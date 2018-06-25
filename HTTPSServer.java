/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import java.io.*;
import java.net.*;
import java.security.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author iflores
 */

//TO DO: 
// 1) Add descriptive comments
// 2) Refractor variable names 

public class HTTPSServer extends Thread {
    
    private HttpsServer server; 
    
    public int buf_len = 2048;
        
    //TO DO: Clean up use of old config variables
    // Old Config variables
    public int port = 9001; 
    private String ksName = "/home/iflores/NetBeansProjects/garageDoorOpener/src/bb_garagedooropener/flores3.jks";            
    char ksPass[] = "floresJKS123!".toCharArray();
    char ctPass[] = "mykey123!".toCharArray();    
    
    //New config variables
    public String httpsPort;
    public String httpsCertPath;
    public String httpsKeyStorePass;
    public String  httpsCertPass;     
    public GarageMqttClient garageMqttClient;
    
    
    
    //Control flags
    boolean serverRunning = false; 
    boolean serverSetup = false; 
            
    //Data stream variables
    public DataOutputStream dos;
    public DataInputStream dis;
    public ObjectOutputStream oos;
    public ObjectInputStream ois; 
   
    
    
    public HTTPSServer( GarageMqttClient garageMqttClient){
        this.garageMqttClient = garageMqttClient;
    }
    
    public HTTPSServer(Config config, GarageMqttClient garageMqttClient){
        this.garageMqttClient = garageMqttClient;
        httpsPort = config.httpsPort;
        httpsCertPath = config.httpsCertPath;
        httpsKeyStorePass =  config.httpsKeyStorePass;
        httpsCertPass =  config.httpsCertPass;
              
    }
    
    //TO DO: Check to make sure config was loaded and check all parameters have been set
    public void setup (){
        if(Main.debugFlag)
            System.out.println("Setting up HTTPS Server..."); 
        try {
            //Load Certificate 
            FileInputStream fin = new FileInputStream(httpsCertPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(fin, httpsKeyStorePass.toCharArray());
            
            //KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, httpsCertPass.toCharArray());
            
            //TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            
            // create https server
            server = HttpsServer.create(new InetSocketAddress(Integer.parseInt(httpsPort)), 0);
                        
            //SSL Context
            SSLContext sc = SSLContext.getInstance("TLS");
            
            // setup the HTTPS context and parameters
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            server.setHttpsConfigurator(new HttpsConfigurator(sc) {
				public void configure(HttpsParameters params) {
					try {
						// initialise the SSL context
						SSLContext c = SSLContext.getDefault();
						SSLEngine engine = c.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());

						// get the default parameters
						SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
						params.setSSLParameters(defaultSSLParameters);
					} catch (Exception ex) {
                                            if(Main.debugFlag) {
                                                System.out.println("HTTPS Server Configuration Execption");
                                                ex.printStackTrace();
                                            }
						
					}
				}
			});
         
            //Set HTTPS Handlers for server        
            server.createContext("/", new Handlers.RootHandler());
            server.createContext("/toggle", new Handlers.toggle(garageMqttClient));
            server.setExecutor(null);
            serverSetup = true; 
                              
        } 
        catch (Exception e) {
            if(Main.debugFlag){
                System.out.println("HTTPS Server Setup Exeception!"); 
                e.printStackTrace();
            }
            serverSetup = false; 
        }
    }
    
    public void start(){
        if(!serverSetup)
            setup(); 
        try{         
                if(Main.debugFlag)
                    System.out.println("Starting HTTPS Server...");
                server.start();
                if(Main.debugFlag)
                    System.out.println("HTTPS Server running on port: " + httpsPort);
                serverRunning = true;
             }
             catch (Exception e) {
                if(Main.debugFlag){
                    System.out.println("HTTPS Server Start Exeception!"); 
                    e.printStackTrace();                
                    System.out.println("waiting 5 secs before attempting to restart...");
                }
                try {                                    
                    Thread.sleep(5000); 
                    serverRunning = false;
                 } catch (InterruptedException ex) {
                    if(Main.debugFlag){ 
                        Logger.getLogger(HTTPSServer.class.getName()).log(Level.SEVERE, null, ex);
                        e.printStackTrace();
                    }
                 }
            }
    }
    
    @Override
    public void run() {
        setup(); 
        start(); 
        
        while(true ){
            
            if(!serverSetup)
                setup();
            if(!serverRunning)
                start();
                                                 
        }                
    }
    
 
}
