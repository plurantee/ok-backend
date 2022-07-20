package com.example.demo.repository;

import com.example.demo.model.User;
import com.example.demo.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUser(User user);
    Optional<UserDetails> findByEmail(String email);
}
