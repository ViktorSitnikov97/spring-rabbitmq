package exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.dto.UserCreateDTO;
import exercise.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import exercise.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    void testCreateUser() throws Exception {
        var dto = new UserCreateDTO("Tirion");

        var result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isPresent(),
                v -> v.node("name").isEqualTo(dto.getName())
        );
        assertThat(userRepository.existsByName(dto.getName())).isTrue();

        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), messageCaptor.capture());
        var message = messageCaptor.getValue();
        assertThat(message.contains(dto.getName())).isTrue();
    }

    @Test
    void testDeleteUser() throws Exception {
        var user = new User();
        user.setName("Jack");
        userRepository.save(user);
        var id = user.getId();

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(userRepository.existsById(id)).isFalse();

        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), messageCaptor.capture());
        var message = messageCaptor.getValue();
        assertThat(message.contains(user.getName())).isTrue();
    }
}