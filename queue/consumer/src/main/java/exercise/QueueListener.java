package exercise;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;
import java.util.ArrayList;


@Component
@Slf4j
public class QueueListener {

    // Для наглядности в список будут добавляться все сообщения,
    // которые получены из очереди
    // Так их можно будет легко просмотреть
    private List<String> messages = new ArrayList<>();

    public List<String> getAllMessages() {
        return messages;
    }

    @RabbitListener(queues = "queue")
    public void processQueue(String message) {
        messages.add(message);
        log.info("Getting messages.....");
        log.info("Received from queue: " + message);
    }
}
