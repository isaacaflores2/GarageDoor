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


/**
 *
 * @author iflores
 */
public class TCPServer extends Thread {
    
    public int port = 5000; 
    public int buf_len = 2048;
    public String filename = "testVideoDownload.mp4";
    public DataOutputStream dos;
    public DataInputStream dis;
    public ObjectOutputStream oos;
    public ObjectInputStream ois; 
    private String ksname = "flores.jks";
  
    
    
    private InetAddress inetAdress;
    private String ip; 
    
    
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            /*Print server IP ADDRESS and PORT */
            System.out.println("Server is ready on address:");
            ip = serverSocket.getInetAddress().toString();
            System.out.println(serverSocket.getInetAddress());
            System.out.println("Server is ready on port:");         
            System.out.println(port);
    
            while (true) {
                Socket s = serverSocket.accept();
                
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
    
    
 
    
    private void saveFile(Socket socket)throws Exception {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        
        FileOutputStream fos = null;
        
        //Read Double from client
       // locationData = (LocationData) ois.readObject();
        System.out.println("Reading... ");
        
        Double d = dis.readDouble();
        // Object o =  ois.readObject();
        
       /* //double d = (double) ois.readObject();
        if(o == null){
            System.out.println("Object from data is null. ");
        }
        else 
       if( o instanceof Double)1{         
            d = (Double) o;
            System.out.println("Data from client is: ");
            System.out.println(d);
        }
        else{
            System.out.println("Data from client is not a LocationData Object. ");
        }
        */
        System.out.println("Data from client is: ");
       // System.out.println(d);
        
        //System.out.println(d);
        System.out.println(d.toString());
            
    }     
}


