package exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    private static final String QUEUE = "queue";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @BeforeEach
    public void clearQueue() {
        rabbitAdmin.purgeQueue(QUEUE);
    }

    @Test
    void testGetMessages() throws Exception {
        var message = "Test message";

        rabbitTemplate.convertAndSend(QUEUE, message);

        var result = mockMvc
                .perform(get("/messages"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        var messages = mapper.readValue(body, new TypeReference<List<String>>() {
        });
        assertThat(messages.size()).isEqualTo(1);
        assertThat(messages.getFirst()).isEqualTo(message);
    }
}

