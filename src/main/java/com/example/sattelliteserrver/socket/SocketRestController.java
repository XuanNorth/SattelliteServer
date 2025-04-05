package com.example.sattelliteserrver.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/remote")
public class SocketRestController {

    @Autowired
    SocketSentService socketSentService;

    String IpGroundStation = "192.168.0.100";

    @PostMapping("/{command}")
    public ResponseEntity<Void> sendCommand(@PathVariable String command) {
        return SocketSentService.sendDataTCP(IpGroundStation,8080,command)
                ?
                ResponseEntity.ok().build()
                :
                ResponseEntity.badRequest().build();
    }


}
