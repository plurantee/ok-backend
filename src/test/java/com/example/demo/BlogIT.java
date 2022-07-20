package com.example.demo;

import com.example.demo.config.JwtAuthenticationEntryPoint;
import com.example.demo.config.JwtRequestFilter;
import com.example.demo.controller.BlogController;
import com.example.demo.model.Blog;
import com.example.demo.model.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.BlogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserDetailsRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = BlogController.class)
public class BlogIT {
    @TestConfiguration
    static class BlogIntegrationTestingConfiguration {
        @Bean
        public JwtTokenUtil jwtTokenUtil() {
            return new JwtTokenUtil();
        }
    }
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthorityRepository authorityRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BlogRepository blogRepository;
    @MockBean
    private UserDetailsRepository userDetailsRepository;
    @MockBean
    private DataSource dataSource;
    @MockBean
    private UserService userService;
    @MockBean
    private EntityManager entityManager;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



    @MockBean
    private EntityManagerFactory entityManagerFactory;

    private String jwt;


    @BeforeEach
    public void setup() throws Exception {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        ReflectionTestUtils.setField(jwtRequestFilter, "jwtTokenUtil", jwtTokenUtil);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        Map<String, Object> claims = new HashMap<>();
        User user = new User("test", "password");
        jwt = jwtTokenUtil.generateToken(user);
    }

    @Test
    public void test() throws Exception {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(webApplicationContext.getBean("blogController"));
    }

    @Test
    public void test_200() throws Exception {
        this.mockMvc.perform(get("/api/blogs").header("Authorization", "Bearer "+jwt))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void test_get_blog_200() throws Exception {
        User user = new User("test", "password");
        Blog blog = new Blog();
        blog.setUser(user);
        List<Blog> blogs = new ArrayList();
        blogs.add(blog);
        Mockito.when(userService.loadUserByUsername(anyString())).thenReturn(user);
        Mockito.when(blogRepository.findByUser(any(User.class))).thenReturn(blogs);
        this.mockMvc.perform(get("/api/blogs").header("Authorization", "Bearer "+jwt))
                .andDo(print()).andExpect(content().json("[{\"id\":null,\"blogDetails\":\"Test\"," +
                        "\"user\":{\"username\":\"test\"},\"isCompleted\":null}]"));
    }

}
