package com.foordermediate.mediate.controller;

import com.foordermediate.mediate.constants.QueueType;
import com.foordermediate.mediate.service.Consumer;
import com.foordermediate.mediate.service.Publisher;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {

    @GetMapping("")
    public Object getHealth(){
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("alive", true);
        return result;
    }

    @PostMapping("/publish-pickup")
    public boolean publishMessage(@RequestBody HashMap<String, Object> req) throws Exception {
        HashMap<String, Object> message = (HashMap<String, Object>) req.get("message");
        boolean published = false;
        try{
            System.out.println(message);
            Publisher.publish(QueueType.PICKUP, String.valueOf(message));
            published = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return published;
    }

    @PostMapping("/consume-pickup")
    public boolean consumeMessage() throws Exception {
        boolean consumed = false;
        try{
            Consumer.consume(QueueType.PICKUP);
            consumed = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return consumed;
    }
}
