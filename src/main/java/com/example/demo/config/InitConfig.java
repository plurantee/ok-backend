package com.example.demo.config;

import com.example.demo.model.Blog;
import com.example.demo.model.MyAuthorities;
import com.example.demo.model.User;
import com.example.demo.model.UserDetails;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.BlogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitConfig {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogRepository blogRepository;

    @Bean
    public void initializer() {
        MyAuthorities userAuth = userAuth();
        MyAuthorities adminAuth = adminAuth();
        authorityRepository.save(userAuth);
        authorityRepository.save(adminAuth());
        String password = passwordEncoder.encode("password");
        User user = new User("florante", password);
        user.addAuthority(adminAuth);
        userRepository.save(user);
        Blog blog = new Blog();
        blog.setBlogTitle("Blog Topic 1");
        blog.setBlogContent("This is the content.... ");
        blog.setUser(user);
        blogRepository.save(blog);
        userDetailsRepository.save(new UserDetails(user, "rapioflorante1@gmail.com"));


    }

    @Bean
    public MyAuthorities adminAuth() {
        return new MyAuthorities("admin", "ROLE_ADMIN");
    }

    @Bean
    public MyAuthorities userAuth() {
        return new MyAuthorities("user", "ROLE_USER");
    }


}
