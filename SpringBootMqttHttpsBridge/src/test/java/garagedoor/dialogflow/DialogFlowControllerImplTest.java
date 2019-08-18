package garagedoor.dialogflow;

import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2WebhookResponse;
import garagedoor.Controllers.DialogFlowControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DialogFlowControllerImplTest {

    @InjectMocks
    private DialogFlowControllerImpl dialogFlowControllerImpl;

    @Mock
    private  DialogFlowRequestHandler requestHandler;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void smartGarageIntent() {

        String requestStr = "{" +
        " \"responseId\": \"56b46b21-9c75-4951-8454-1daf9a971a28-13076db6\","+
        " \"queryResult\": {" +
        " \"queryText\": \"open\", " +
        " \"parameters\": { " +
        " \"actions\": \"open\" " +
        " }," +
        "    \"allRequiredParamsPresent\": true," +
        "    \"fulfillmentText\": \"Sorry I cannot control your garage  right now.\"," +
        "    \"fulfillmentMessages\": [" +
        "{" +
        "\"text\": {" +
        " \"text\": [" +
        "   \"Sorry I cannot control your garage  right now.\"" +
        "  ]" +
        "}" +
        "}" +
        "]," +
        "    \"intent\": {" +
        "    \"name\": \"projects/garage-b86e6/agent/intents/2469bce3-abbe-4213-ace0-dbf4e4e48cd9\"," +
        "    \"displayName\": \"ControlGarageDoor\"," +
        "   \"endInteraction\": true" +
        "   }," +
        "   \"intentDetectionConfidence\": 0.5030417," +
        "  \"languageCode\": \"en\"" +
        "   }," +
        "   \"originalDetectIntentRequest\": {" +
        "   \"payload\": {}" +
        "   }, " +
        "   \"session\": \"projects/garage-b86e6/agent/sessions/60e206c6-f93f-b309-cc63-02ea99a602a1\"" +
        "   }";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("actions", DialogFlowService.ACTION_OPEN);
        when(requestHandler.processRequest(parameters)).thenReturn(DialogFlowService.RESPONSE_ACTION_OPEN);
        ResponseEntity<GoogleCloudDialogflowV2WebhookResponse> responseEntity = (ResponseEntity<GoogleCloudDialogflowV2WebhookResponse>) dialogFlowControllerImpl.smartGarageIntent(requestStr);
        //verify(requestHandler).processRequest(parameters);

        GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
        response.setFulfillmentText(DialogFlowService.RESPONSE_ACTION_OPEN);
        ResponseEntity<GoogleCloudDialogflowV2WebhookResponse> expectedResponsEntity = new ResponseEntity<GoogleCloudDialogflowV2WebhookResponse>(response, HttpStatus.OK);

        assertEquals(responseEntity,expectedResponsEntity );
    }
}