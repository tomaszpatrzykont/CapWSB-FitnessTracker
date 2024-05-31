package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping("/getuser")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @PostMapping("/adduser")
    public User addUser(@RequestBody UserDto userDto) {
        // Przy wpisywaniu age możemy ustawić 0 - wiek zostanie automatycznie wyliczony
        // Demonstracja how to use @RequestBody
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        // TODO: saveUser with Service and return User
        User usertoadd = userMapper.toEntity(userDto);
        return userService.createUser(usertoadd);
    }

    @DeleteMapping("/deleteuser")
    public void deleteUser(@RequestParam("id") Long id) throws UserNotFoundException {
        Optional<User> optionalUser = userService.getUser(id);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();
            userService.deleteUser(userToDelete.getId());
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @GetMapping("/finduserbyemail")
    public List<UserDto> getUsersByEmail(@RequestParam(value = "email", required = false) String email) {
        return userService.findAllUsers()
                .stream()
                .filter(user -> email == null || user.getEmail().equalsIgnoreCase(email))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/finduserbyage")
    public List<UserDto> getUsersByAge(@RequestParam(value = "age", required = true) Integer age) {
        return userService.findAllUsers()
                .stream()
                .filter(user -> user.getAge() > age)
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    @PutMapping("/updateuser/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {

        User existingUser = userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setFirstName(userDto.firstName());
        existingUser.setLastName(userDto.lastName());
        existingUser.setBirthdate(userDto.birthdate());
        existingUser.setEmail(userDto.email());
        existingUser.setAge(userDto.age());

        return userService.updateUser(id, existingUser);
    }


}