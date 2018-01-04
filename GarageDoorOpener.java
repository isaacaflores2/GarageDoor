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
public class GarageDoorOpener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new GarageDoorServer().start();
        new GarageMqttClient().start();
        
        //GarageDoorServer mGarageDoorServer = new GarageDoorServer(); 
        
    }
    
}
