package com.example.Final.repo;

import com.example.Final.Enum.Role;
import com.example.Final.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Userepo extends JpaRepository<User,Long> {
    long count();
    User findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);

    List<User> findByFirstnameContainingIgnoreCase(String firstname);
    List<User> findByLastnameContainingIgnoreCase(String lastname);
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByEmailContainingIgnoreCase(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);

}
