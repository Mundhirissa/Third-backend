package com.example.Final.controller;

import com.example.Final.Enum.Role;
import com.example.Final.dto.Loginrequest;
import com.example.Final.model.Stadiumstaff;
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
    public ResponseEntity<?> login(@RequestBody Loginrequest loginRequest) {
        Optional<User> userOptional = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", user.getUsername());
            response.put("role", user.getRole().name()); // Get the name of the enum value

            // Check if the role is STAFF and the user is an instance of Stadiumstaff
            if (user.getRole() == Role.staff && user instanceof Stadiumstaff) {
                Stadiumstaff staff = (Stadiumstaff) user;
                response.put("stadiumId", staff.getStadium() != null ? staff.getStadium().getStadiumid() : null);
            }

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



    @PostMapping("/create/admin")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        User createdAdmin = userService.createAdminUser(user);
        return ResponseEntity.ok(createdAdmin);
    }


}
