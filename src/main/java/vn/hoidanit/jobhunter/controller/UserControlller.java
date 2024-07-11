package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.config.SecurityConfiguration;
import vn.hoidanit.jobhunter.domain.RestResponse;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserControlller {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserControlller(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> CreateUser(@RequestBody User userJSON) {
        String encodedPassword = this.passwordEncoder.encode(userJSON.getPassword());
        userJSON.setPassword(encodedPassword);
        User newUser = userService.saveUser(userJSON);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestResponse<Object>> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("id không tồn tại");
        }
        this.userService.deleteUser(id);

        RestResponse<Object> response = new RestResponse<>();
        response.setMessage("Đã xóa user có id là: " + id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> GetUser() {
        return ResponseEntity.ok(this.userService.GetAllUser());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> GetUserByID(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.userService.GetUserByID(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> UpdateUser(@PathVariable("id") long id, @RequestBody User updateUser) {
        User user = this.userService.GetUserByID(id);
        user.setId(id);
        user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setPassword(updateUser.getPassword());
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
