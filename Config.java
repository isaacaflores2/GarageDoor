/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;
import java.io.DataInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.Scanner;


/**
 *
 * @author iflores
 * This class reads the configuration file to run the mqtt client and the https bridge. The file contains X lines for the following parameters:
        1. mykey
        2. debug
        3. mqtt params
        4. https server params: ip address, port, cert path, ct pass, kt pass
 */
public class Config {
    
    //Path to Configuration File
    public String configFilePath;
    
    /*Config constants used to read command line arugments and parameters from
    the config file
    */    
    ///Debug flag to allow debug and error messages to print to stdout
    public static String debugArg  = "-d";
    ///Command line argument to load specified config file
    public static String configFilePathArg = "-c";
    ///Help flag prints command line argument usage
    public static String helpArg = "-h";
    
    //MQTT Client Parameters
    public static String mqttTopicArg = "mqttTopic";
    public static String mqttBrokerIPAddressArg = "mqttBrokerIPAddress"; // "ssl://ipaddres:port or "ssl://url:port "
    public static String mqttClientIdArg = "mqttClientID";
    public static String mqttUsernameArg = "mqttUsername";
    public static String mqttPasswordArg = "mqttPassword";
    
    //HTTPS Server Parameters
    public static String httpsPortArg = "httpsPort";
    public static String httpsCertPathArg = "httpsCertPath";
    public static String httpsKeyStorePassArg = "httpsKeyStorePass";
    public static String  httpsCertPassArg = "httpsCertPass"; 

    //HTTPS Handler Parameters
    public static String httpsAuthKeyArg = "httpsAuthKey"; 
         
    //MQTT Client Parameters
    public String mqttTopic;
    public String mqttBrokerIPAddress; // "ssl://ipaddres:port or "ssl://url:port "
    public String mqttClientId;
    public String mqttUsername;
    public String mqttPassword;
    
    //HTTPS Server Parameters
    public String httpsPort;
    public String httpsCertPath;
    public String httpsKeyStorePass;
    public String  httpsCertPass; 

    //HTTPS Handler Parameters
    public String httpsAuthKey; 
    
    //Flag to check if config file has been loaded
    public boolean configLoaded;
    
    /// Read and load configurations file 
    public Config(){
        configFilePath = null;
        configLoaded = false;
    }
    
    public Config(String path){
        this.configFilePath = path;
        configLoaded = false;
    }
    
    
    
    public Boolean loadParameter(String param, String paramValue)
    {
        //Ensure param and paramValue are not null
        if(param == null){
            System.out.println("param is null. check config file");
            return  false; 
        }        
        if(paramValue == null ){
            System.out.println("param value is null. check config file");
            return false;            
        }
        
        //TO DO: Check if all param values for a certain format
        if(param.equals(mqttTopicArg))
        {
            mqttTopic = paramValue;
        }
        else if(param.equals(mqttBrokerIPAddressArg))
        {
            mqttBrokerIPAddress = paramValue;
        }
        else if(param.equals(mqttClientIdArg))
        {
            mqttClientId = paramValue;
        
        }
        else if(param.equals(mqttUsernameArg))
        {
            mqttUsername = paramValue;
        }
        else if(param.equals(mqttPasswordArg))
        {
            mqttPassword = paramValue;
        }
        else if(param.equals(httpsPortArg))
        {
            httpsPort = paramValue;
        }
        else if(param.equals(httpsCertPathArg))
        {
            httpsCertPath = paramValue;
        }
        else if(param.equals(httpsKeyStorePassArg))
        {
            httpsKeyStorePass = paramValue;
        } 
        else if(param.equals(httpsCertPassArg))
        {
            httpsCertPass = paramValue;
        } 
        else
        {
            if(param.equals(httpsAuthKeyArg))
                httpsAuthKey = paramValue; 
        }
        return true;
    }
    
    public Boolean loadConfig(String configFilePath)
    {
        try
            {
                File configFile = new File(configFilePath);
                Scanner configFileScanner = new Scanner(configFile);                
                String paramLine[];
                
                while( configFileScanner.hasNextLine() )
                {                    
                    paramLine = configFileScanner.nextLine().split("=");
                    loadParameter( paramLine[0], paramLine[1]);               
                }
            }
            catch(Exception e)
            {
              System.out.print(e.toString());
              printConfigHelp();
            }
        configLoaded = true;
        return true;
    }
    
    public static void printConfigHelp()
    {
        System.out.println("Garage Mqtt-Https Bridge version 1.0 build date 24 June 2018");
        System.out.println("Usage: garage [-d] [-c] [-h]");
        System.out.println(
                  "  -d: debug mode. enables all system prints and debug. \n" 
                + "  -c: specify the garage config file. \n"
                + "  -h prints this help. \n");
    }
}
