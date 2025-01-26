package exercise.controller;

import exercise.dto.UserCreateDTO;
import exercise.dto.UserDTO;
import exercise.mapper.UserMapper;
import exercise.model.User;
import exercise.repository.UserRepository;
import exercise.MessageSender;
import exercise.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private UserMapper mapper;

    @GetMapping(path = "")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserCreateDTO dto) {
        var user = mapper.map(dto);
        userRepository.save(user);
        String message = "User " + user.getName() + " has been registered";
        // BEGIN (write your solution here)
        messageSender.send(message);
        // END
        return mapper.map(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {

        var userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String message = "User " + userToDelete.getName() + " has been deleted";
        userRepository.delete(userToDelete);
        // BEGIN (write your solution here)
        messageSender.send(message);
        // END
    }
}
