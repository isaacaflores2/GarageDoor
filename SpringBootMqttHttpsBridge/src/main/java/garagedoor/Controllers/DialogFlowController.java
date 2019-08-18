package garagedoor.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DialogFlowController {
    @PostMapping("/dialogFlowWebHook")
    public ResponseEntity<?> smartGarageIntent(@RequestBody String requestStr);
}
