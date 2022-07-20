package com.example.demo.controller;

import com.example.demo.exception.AuthException;
import com.example.demo.model.AuthRequest;
import com.example.demo.model.AuthResponse;
import com.example.demo.model.MyAuthorities;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.model.UserDetails;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserDetailsRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MyAuthorities userAuth;

    @PostMapping("login")
    public ResponseEntity auth(@RequestBody AuthRequest authRequest) throws Exception {
        authenticate(authRequest.getUsername(), authRequest.getPassword());
        User user = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("register")
    public ResponseEntity authRegister(@RequestBody RegisterRequest registerRequest) throws Exception {
        Optional<User> opUser = userRepository.findByUsername(registerRequest.getUsername());
        Optional<UserDetails> emailCheck = userDetailsRepository.findByEmail(registerRequest.getEmail());
        if (opUser.isPresent()) {
            throw new AuthException("User already exists");
        }
        if (emailCheck.isPresent()) {
            throw new AuthException("User with the same email already exists");
        }
        User user = new User(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
        UserDetails userDetails = new UserDetails(user, registerRequest.getEmail());
        user.addAuthority(userAuth);
        userRepository.save(user);
        userDetailsRepository.save(userDetails);
        return ResponseEntity.ok(user);

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException("Auth Failed");
        }
    }
}
