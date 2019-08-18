package garagedoor.dialogflow;

import garagedoor.Controllers.BridgeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DialogFlowService implements DialogFlowRequestHandler {

    public final static String ACTION_OPEN = "open";
    public final static String ACTION_CLOSE = "close";
    public final static String ACTION_STATUS = "status";
    public final static String DOOR_STATUS_OPEN = "open";
    public final static String DOOR_STATUS_CLOSED = "closed";
    public final static String RESPONSE_ACTION_OPEN = "Opening your garage now.";
    public final static String RESPONSE_ACTION_CLOSE = "Closing your garage now.";


    @Autowired
    BridgeController bridgeController;


    private Logger logger;

    @Override
    public String processRequest(Map<String, Object> request) {
        String action = (String) request.getOrDefault("actions", null);
        this.logger = LoggerFactory.getLogger(DialogFlowRequestHandler.class);
        String response;

        if(action != null )
        {
            switch(action){
                case ACTION_OPEN:
                    bridgeController.toggle("1");
                    response = "Opening your garage now.";
                    break;
                case ACTION_CLOSE:
                    bridgeController.toggle("1");
                    response = "Closing your garage now.";
                    break;
                case ACTION_STATUS:
                    String garageStatus = bridgeController.getDeviceData("2");


                    if( garageStatus.equals(DOOR_STATUS_OPEN)){
                        response = "Your garage is " + garageStatus + ".";
                        response += " Would you like me to close your garage?";
                    }
                    else if (garageStatus.equals(DOOR_STATUS_CLOSED)){
                        response = "Your garage is " + garageStatus + ".";
                        response += " No need to worry, your house is safe.";
                    }
                    else{
                        response = garageStatus;
                    }
                    break;
                default:
                    response = "Sorry I have received an action I cannot help with.";
                    logger.info(response);
            }
        }
        else{
            response = response = "Sorry I did not receive an action with your request.";
            logger.info(response);
        }
        return response;
    }
}
