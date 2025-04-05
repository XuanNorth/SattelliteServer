package com.example.sattelliteserrver.websocket;

import com.example.sattelliteserrver.data.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addNewData(DataEntity data){
        messagingTemplate.convertAndSend("/topic/data", data);
    }

    public void updatePosture(Double roll , Double pit, Double yaw){
        messagingTemplate.convertAndSend("/topic/posture", roll + "-" + pit + "-" + yaw);

    }

}
