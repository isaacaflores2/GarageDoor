package garagedoor.Controllers;

import com.google.actions.api.DialogflowApp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookResponse;
import garagedoor.dialogflow.DialogFlowRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DialogFlowControllerImpl extends DialogflowApp implements DialogFlowController{

    @Autowired
    private DialogFlowRequestHandler requestHandler;

    private static JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    @Override
    public ResponseEntity<?> smartGarageIntent(@RequestBody String requestStr) {
        try {

            GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
            GoogleCloudDialogflowV2WebhookRequest request = jacksonFactory.createJsonParser(requestStr).parse(GoogleCloudDialogflowV2WebhookRequest.class);
            Map<String, Object> parameters = request.getQueryResult().getParameters();

            if (parameters != null && parameters.size() > 0) {
                response.setFulfillmentText(requestHandler.processRequest(parameters));

            } else {
                response.setFulfillmentText("Sorry your request did not have any information.");
            }

            return new ResponseEntity<GoogleCloudDialogflowV2WebhookResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
