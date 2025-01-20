package exercise.controller;

import exercise.QueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private QueueListener listener;

    @GetMapping()
    public List<String> getAllMessages() {
        return listener.getAllMessages();
    }
}
