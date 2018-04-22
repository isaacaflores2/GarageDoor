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

import java.net.InetAddress;
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
public class HTTPSServer extends Thread {
    private HttpsServer server; 
    public static int port = 9000; 
    public int buf_len = 2048;
    public DataOutputStream dos;
    public DataInputStream dis;
    public ObjectOutputStream oos;
    public ObjectInputStream ois; 
    private String ksName = "/home/iflores/NetBeansProjects/garageDoorOpener/src/garagedooropener/flores2.jks";
    //private String ksName = "/home/debian/GarageDoor/flores2.jks";
    char ksPass[] = "floresJKS123!".toCharArray();
    char ctPass[] = "mykey123!".toCharArray();
    public GarageMqttClient garageMqttClient;
    boolean serverRunning = false; 
    boolean serverSetup = false; 
    
    public HTTPSServer( GarageMqttClient garageMqttClient){
        this.garageMqttClient = garageMqttClient;
    }
    
    public void setup (){
        System.out.println("Setting up HTTPS Server..."); 
        try {
            //Load Certificate 
            FileInputStream fin = new FileInputStream(ksName);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(fin, ksPass);
            
            //KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ctPass);
            
            //TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            
            // create https server
            server = HttpsServer.create(new InetSocketAddress(port), 0);
                        
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
                                            System.out.println("HTTPS Server Configuration Execption");
                                            ex.printStackTrace();
						
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
            System.out.println("HTTPS Server Setup Exeception!"); 
            e.printStackTrace();
            serverSetup = false; 
        }
    }
    
    public void start(){
        if(!serverSetup)
            setup(); 
        try{                
                System.out.println("Starting HTTPS Server...");
                server.start();
                System.out.println("HTTPS Server running on port: " + port);
                serverRunning = true;
             }
             catch (Exception e) {
                System.out.println("HTTPS Server Start Exeception!"); 
                e.printStackTrace();
                
                System.out.println("waiting 5 secs before attempting to restart...");
                try {                                    
                    Thread.sleep(5000); 
                    serverRunning = false;
                 } catch (InterruptedException ex) {
                    Logger.getLogger(HTTPSServer.class.getName()).log(Level.SEVERE, null, ex);
                    e.printStackTrace();
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
    
    /*public static void main(String[] args) { 
      new HTTPSServer().start(); 
   }
*/
}
