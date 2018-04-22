/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



/**
 *
 * @author iflores
 */
public class Handlers {
     
    public static class RootHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
                        
			String response = "<h1>Server start success if you see this message</h1>" + "<h1>Port: " + HTTPSServer.port + "</h1>";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
    
    public static class toggle implements HttpHandler {
                private GarageMqttClient garageMqttClient;
                
                public toggle( GarageMqttClient garageMqttClient){
                    this.garageMqttClient = garageMqttClient; 
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
                            System.out.println(response);                       
                            //Publish MQTT Topic
                            garageMqttClient.publish("garage/toggle", response);
                        }
                        else{
                          System.out.println("Client without valid key attempted to connect");                         
                        }
		}
                
                public boolean validateClient(URI uri){
                    String url = uri.toString();
                    int indexOfKey = url.indexOf("/", 1);
                    String key = url.substring(indexOfKey+1);
                    if( (key.equals( Main.mykey)) ) 
                        return true;
                    return false;
                }
                
	}
    
    
    
}
