package com.example.sattelliteserrver.serial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")

public class SerialController {


    private final SerialService serialService;


    public SerialController( SerialService serialService) {
        this.serialService = serialService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCommand(@RequestBody String command) {
        try {

            serialService.send(command);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}