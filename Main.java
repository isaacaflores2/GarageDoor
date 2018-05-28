/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;



/**
 *
 * @author iflores
 */
public class Main {
    public final static String mykey = "secretkey	";
    public static boolean debug = false; 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic herea
        
        if( args.length > 0)
            debug = Boolean.valueOf(args[0]);
        
        GarageMqttClient garageMqttClient = new GarageMqttClient(); 
        garageMqttClient.start();
        HTTPSServer httpsServer = new HTTPSServer(garageMqttClient);
        httpsServer.start();                     
        
        
        //CameraClient camera = new CameraClient(0);
        //Application.launch(CameraClient.class, args);
    }
    
}
