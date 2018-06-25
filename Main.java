package bb_garagedooropener;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;


/**
 *
 * @author Isaac Flores June 2018
 * - Main class initiates the two servers (HTTPS and MQTT Client) that 
 * make up our HTTPS-MQTT Bridge. 
 * - The main class creates an instace of the config class with loads a config
 * file. The default file is titled "garage_config.txt" Use this file to 
 * format your own custom config file. 
 * - To do: 1) Complete readConfig class to take in parameters from a file
 * 2) Remove remaining string literals
 * 3) Add descriptive comments for each class and methods
 */
public class Main {
    
    //Debug flag to allow debug and error messages to print to stdout
    public static boolean debugFlag = false;
    
    public final static String mykey = "smarthomeoptimis13!";
    private final static String topic = "garage/toggle";
    private final static String broker       = "ssl://isaacaflores2.myddns.rocks:8883";
    private final static String clientId     = "httpsBridge";
    private final static String username = "iflores";
    private final static String password = "smarthomeoptimis13!";

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Create instance of configuration class
        Config config = new Config(); 

        if(args.length == 0 )
        {
            //Read config from default directory (default directory is the current working directory)
            config.loadConfig("/home/iflores/NetBeansProjects/garageDoorOpener/src/bb_garagedooropener/garage_config.txt");          
        }        
        else 
        {   
            for(int i=0; i < args.length; i++)
            {
                if(args[i].contentEquals(Config.debugArg) )
                    debugFlag = true;

                if(args[i].contentEquals(Config.configFilePathArg))
                {
                    //Read config file
                    config.loadConfig(args[i+1]);
                    
                    //progress counter past file path to next arguments
                    i++;
                }
                
                if(args[i].contentEquals(config.helpArg))
                {
                    config.printConfigHelp();
                }
            }            
            
            //If config file path command line argument was not set. Load default config file
            config.loadConfig("/home/iflores/NetBeansProjects/garageDoorOpener/src/bb_garagedooropener/garage_config.txt");          
        }
            
        //Start MqttClient
        //GarageMqttClient garageMqttClient = new GarageMqttClient(topic, clientId, username, password, broker); 
        GarageMqttClient garageMqttClient = new GarageMqttClient(config); 
        //GarageMqttClient garageMqttClient = new GarageMqttClient(); 
        garageMqttClient.start();
  
        //Start HTTPS Server
        HTTPSServer httpsServer = new HTTPSServer(config, garageMqttClient);
        //HTTPSServer httpsServer = new HTTPSServer(garageMqttClient);        
        httpsServer.start();                     
        
        
       
    }
    
}
