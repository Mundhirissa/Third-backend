package com.example.Final.services;

import com.example.Final.Enum.Role;
import com.example.Final.model.User;
import com.example.Final.repo.Userepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private Userepo userepo;

    @Autowired
    PasswordEncoder passwordEncoder;

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
            User user1 = new User();
        user1.setFirstname(user.getFirstname());
        user1.setLastname(user.getLastname());
        user1.setEmail(user.getEmail());
        user1.setRole(Role.user);
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));



        return userepo.save(user1);
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
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, user.getPassword())) {
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




    public User createAdminUser(User user) {
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

        // Create a new user instance and set the properties
        User user1 = new User();
        user1.setFirstname(user.getFirstname());
        user1.setLastname(user.getLastname());
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set the role as 'ADMIN' explicitly
        user1.setRole(Role.admin);

        return userepo.save(user1);
    }





    @PostConstruct
    public void init() {
        if (userepo.countByRole(Role.admin) == 0) {
            User defaultAdmin = new User();
            defaultAdmin.setFirstname("Admin");
            defaultAdmin.setLastname("User");
            defaultAdmin.setUsername("admin");
            defaultAdmin.setEmail("admin@example.com");
            defaultAdmin.setPassword(passwordEncoder.encode("1"));
            defaultAdmin.setRole(Role.admin);

            userepo.save(defaultAdmin);
        }
    }


}
