/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedooropener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;


/**
 *
 * @author iflores
 */


public class GarageDoorServer extends TCPServer {
    private boolean isDoorOpen; 
    private boolean doorButton; 
    public Commands command; 

 
    
    
    public boolean toggleDoor(){
        doorButton = !doorButton;
        return doorButton;        
    }
    
    public boolean isDoorOpen(){
        return isDoorOpen; 
    }
    
    
 
    @Override
    public void serverFunction(Socket socket) throws Exception{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        
        System.out.println("WAITING ON COMMAND");
       
        int c = (int) dis.readByte();
        
        
        switch(c){
            case 0:
                System.out.println("TOGGLE GARAGE DOOR");
                toggleDoor(); 
                break;
            case 1: 
                System.out.println("IS DOOR OPEN");             
                dos.writeByte(0);                         
                System.out.println("door value sent");

                
                break;
            default: 
                System.out.println("BAD COMMAND");
                break;                    
        }      
    }

    
    
}


