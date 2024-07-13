package com.example.Final.services;

import com.example.Final.model.User;
import com.example.Final.repo.Userepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private Userepo userepo;

    public List<User> getAllUsers() {
        return userepo.findAll();
    }

    public Optional<User> getUserById(Long userid) {
        return userepo.findById(userid);
    }

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Check if the user already exists by username or email
        User existingUser = userepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }

        return userepo.save(user);
    }


    public User updateUser(Long userid, User userDetails) {
        User user = userepo.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userepo.save(user);
    }

    public void deleteUser(Long userid) {
        userepo.deleteById(userid);
    }


    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = userepo.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public List<User> searchUsers(String keyword) {
        return userepo.findByFirstnameContainingIgnoreCase(keyword);
    }


    public long countTotalUsers() {
        return userepo.count();
    }



    public Optional<User> getUserByUsername(String username) {
        return userepo.findByUsername(username);
    }


}
