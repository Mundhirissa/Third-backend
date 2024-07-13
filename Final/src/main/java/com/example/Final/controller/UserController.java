package com.example.Final.controller;

import com.example.Final.dto.Loginrequest;
import com.example.Final.model.User;
import com.example.Final.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins ="http://localhost:3000/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userid}")
    public Optional<User> getUserById(@PathVariable Long userid) {
        return userService.getUserById(userid);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userid}")
    public User updateUser(@PathVariable Long userid, @RequestBody User userDetails) {
        return userService.updateUser(userid, userDetails);
    }

    @DeleteMapping("/{userid}")
    public void deleteUser(@PathVariable Long userid) {
        userService.deleteUser(userid);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Loginrequest loginrequest) {
        Optional<User> userOptional = userService.login(loginrequest.getUsername(), loginrequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Create a response object that includes the username and role
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String keyword) {
        return userService.searchUsers(keyword);
    }


    @GetMapping("/count")
    public long countTotalUsers() {
        return userService.countTotalUsers();
    }



    @GetMapping("/username/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }


}
