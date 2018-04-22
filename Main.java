/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedooropener;

/**
 *
 * @author iflores
 */
public class Main {
    public final static String mykey = "smarthomeoptimis13!";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic herea
        GarageMqttClient garageMqttClient = new GarageMqttClient(); 
        garageMqttClient.start(); 
        HTTPSServer httpsServer = new HTTPSServer(garageMqttClient);
        httpsServer.start();
        
        //new GarageDoorServer().start();
        
        
        //GarageDoorServer mGarageDoorServer = new GarageDoorServer(); 
        
    }
    
}
