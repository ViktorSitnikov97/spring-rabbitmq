package exercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MessageSender {

    // Класс, который дает простой доступ к брокеру сообщений RabbitMQ
    // Позволяет отправлять и получать сообщения
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // BEGIN (write your solution here)
    public void send(String message) {
        log.info("Sending message to the queue...");
        rabbitTemplate.convertAndSend("exchange","key", message);
        log.info("[{}]", message);
        // Отправка сообщения в очередь
        log.info("Message sent successfully to the queue!!!");
    }
    // END
}
