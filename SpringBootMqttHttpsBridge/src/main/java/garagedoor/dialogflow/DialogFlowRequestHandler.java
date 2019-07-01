package garagedoor.dialogflow;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface DialogFlowRequestHandler {
    public String processRequest(Map<String, Object> request);
}
