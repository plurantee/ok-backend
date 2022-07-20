package com.example.demo.service;

import com.example.demo.exception.AuthException;
import com.example.demo.model.User;
import com.example.demo.model.UserDetails;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isPresent()) {
            return opUser.get();
        }
        throw new AuthException("Username not found");
    }

    public UserDetails getUserDetails(User user) throws Exception {
        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findByUser(user);
        if (userDetailsOptional.isPresent()) {
            return userDetailsOptional.get();
        }
        throw new Exception("User settings does not exist");
    }
}
