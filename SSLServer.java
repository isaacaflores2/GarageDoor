/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedooropener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;


/**
 *
 * @author iflores
 */
public class SSLServer extends Thread{
    public int port = 5000; 
    public int buf_len = 2048;
    public DataOutputStream dos;
    public DataInputStream dis;
    public ObjectOutputStream oos;
    public ObjectInputStream ois; 
    private String ksName = "flores.jks";
    char ksPass[] = "floresJKS123!".toCharArray();
    char ctPass[] = "mykey123!".toCharArray();
    
    @Override
    public void run() {
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
            
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
            printServerSocketInfo(serverSocket);

                        
            while (true) {
                SSLSocket s = (SSLSocket) serverSocket.accept();
                printSocketInfo(s);
                //Server Code Below (What the server will do after a client connects)
                System.out.println("Client Connected");
                serverFunction(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void serverFunction(Socket socket) throws Exception{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        System.out.println("default serverFunction");

        Double d = dis.readDouble();
    }
    
      private static void printSocketInfo(SSLSocket s) {
      System.out.println("Socket class: "+s.getClass());
      System.out.println("   Remote address = "
         +s.getInetAddress().toString());
      System.out.println("   Remote port = "+s.getPort());
      System.out.println("   Local socket address = "
         +s.getLocalSocketAddress().toString());
      System.out.println("   Local address = "
         +s.getLocalAddress().toString());
      System.out.println("   Local port = "+s.getLocalPort());
      System.out.println("   Need client authentication = "
         +s.getNeedClientAuth());
      SSLSession ss = s.getSession();
      System.out.println("   Cipher suite = "+ss.getCipherSuite());
      System.out.println("   Protocol = "+ss.getProtocol());
   }
   private static void printServerSocketInfo(SSLServerSocket s) {
      System.out.println("Server socket class: "+s.getClass());
      System.out.println("   Socket address = "
         +s.getInetAddress().toString());
      System.out.println("   Socket port = "
         +s.getLocalPort());
      System.out.println("   Need client authentication = "
         +s.getNeedClientAuth());
      System.out.println("   Want client authentication = "
         +s.getWantClientAuth());
      System.out.println("   Use client mode = "
         +s.getUseClientMode());
   } 
}
    
    

