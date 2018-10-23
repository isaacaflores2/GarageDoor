/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



/**
 *
 * @author iflores
 */
public class Handlers {
        
    public static String[] splitPath(URI uri)
    {
         String url = uri.toString();         
         return url.split("/");
    }
    
    public static class RootHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
                        
                        
			String response = "<h1>Server start success if you see this message</h1>";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
    
    public static class toggle implements HttpHandler {
                private GarageMqttClient garageMqttClient;
                private String mySecretKey;
                
                public toggle( GarageMqttClient garageMqttClient, String mySecretKey){
                    this.garageMqttClient = garageMqttClient; 
                    this.mySecretKey = mySecretKey;
                }
                            
                
		@Override
		public void handle(HttpExchange he) throws IOException {                                                
			URI uri = he.getRequestURI();                      
                        
                        if( validateClient(uri)) {
                            String url = uri.toString();
                            
                            String response = "<h1> The garage door toggle. </h1>";
                            he.sendResponseHeaders(200, response.length());
                            OutputStream os = he.getResponseBody();                                             
                            os.write(response.getBytes());
                            os.close();
                            System.out.println("Garage toggle request from: " + returnRemoteClientInformation(he) );                       
                            //Publish MQTT Topic
                            garageMqttClient.publish("garage/toggle", "Garage door toggle");
                        }
                        else{
                            if(Main.debugFlag)
                            {
                                System.out.println("Client without valid key attempted to connect...");    
                                System.out.println("Client used" + uri.toString());    
                            }
                                
                        }
		}
                
                public String returnRemoteClientInformation(HttpExchange he) {
                    String clientAddress = he.getRemoteAddress().toString();
                    return clientAddress; 
                }
                
                public boolean validateClient(URI uri){
                    String url = uri.toString();
                    int indexOfKey = url.indexOf("/", 1);
                    String key = url.substring(indexOfKey+1);
                    if( (key.equals( mySecretKey)) ) 
                        return true;
                    return false;
                }
                
	}
    
     public static class status implements HttpHandler {
                private GarageMqttClient garageMqttClient;
                private String mySecretKey;
                                
                public status( GarageMqttClient garageMqttClient, String mySecretKey)
                {
                    this.garageMqttClient = garageMqttClient;                     
                    this.mySecretKey = mySecretKey;
                }
                       
            
		@Override
		public void handle(HttpExchange he) throws IOException {
                        String response;
                        
                        if( garageMqttClient.getClientStatus() ){
                            response = "<h1> MQTT Client is running </h1>";
                        }
                        else                             
                            response = "<h1> MQTT Client is not running....FIX IT</h1>";
                        
			he.sendResponseHeaders(200, response.length());                  
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
    
     public static class sensorDataRequest implements HttpHandler 
     {
                private GarageMqttClient garageMqttClient;
                private String mySecretKey;
                private String[] subPaths; 
                                
                public sensorDataRequest( GarageMqttClient garageMqttClient, String mySecretKey)
                {
                    this.garageMqttClient = garageMqttClient;                     
                    this.mySecretKey = mySecretKey;
                    this.subPaths = null ; 
                }
                       
                                
                public sensorDataRequest( GarageMqttClient garageMqttClient){
                    this.garageMqttClient = garageMqttClient;                     
                }
                
                public String getSensorName(URI uri)
                {                    
                    String url = uri.toString();
                    int indexOfKey = url.indexOf("/", 1);
                    return url.substring(indexOfKey+1);
                }
                
                public boolean validateClient(URI uri){
                    String url = uri.toString();
                    int indexOfKey; 
                    indexOfKey = url.indexOf("/", 7);
                    String path = url.substring(indexOfKey+1);
                    indexOfKey = path.indexOf("/", 1);
                    String key = path.substring(indexOfKey+1);
                    
                    if( (key.equals( mySecretKey)) ) 
                        return true;
                    return false;
                }
                
                
                
		@Override
		public void handle(HttpExchange he) throws IOException {       
                        String response = null;
                        this.subPaths = Handlers.splitPath(he.getRequestURI());
                        
                        System.out.println(this.subPaths[0]);
                      
                        /*
                        for(int i = 0; i<this.subPaths.length; i++)
                        {
                            System.out.print(i + ": ");
                            System.out.println(this.subPaths[i]);
                        }
                        */
                       
                        String sensorName;
                        //Get sensor name (topic name)
                        if(subPaths[3].equals(mySecretKey)){
                                                        
                            sensorName = subPaths[2];
                            response = garageMqttClient.getSensorStatus("garage/sensor/" + sensorName, sensorName);
                        }
                        else
                        {
                            response = "You do not have access to this data";                            
                        }
                        
                        /*
                        if( garageMqttClient.getClientStatus(sensorName) ){
                            response = "<h1> MQTT Client is running </h1>";
                        }
                        else                             
                            response = "<h1> MQTT Client is not running....FIX IT</h1>";
                        */
                        
			he.sendResponseHeaders(200, response.length());                  
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
                
                
                
	}
    
}
