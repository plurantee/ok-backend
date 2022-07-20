package com.example.demo.controller;

import com.example.demo.exception.BlogException;
import com.example.demo.model.Blog;
import com.example.demo.model.UpdateBlogRequest;
import com.example.demo.model.User;
import com.example.demo.repository.BlogRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("api/blogs")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity getBlogs(HttpServletRequest request) {

        return ResponseEntity.ok(blogRepository.findAll());
    }

    @PostMapping
    public ResponseEntity createBlog(HttpServletRequest request, @RequestBody Blog blogRequest) throws Exception {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);

        User user = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));


        blogRequest.setUser(user);
        blogRepository.save(blogRequest);
        return ResponseEntity.ok(blogRequest);
    }

    @PutMapping
    public ResponseEntity updateBlog(HttpServletRequest request, @RequestBody Blog blogRequest) throws Exception {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        User user = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));
        Optional<Blog> opBlog = blogRepository.findById(blogRequest.getId());
        Blog blog = null;
        if (opBlog.isPresent()) {
            blog = opBlog.get();
        } else {
            throw new BlogException("Blog does not exist");
        }
        if (!jwtTokenUtil.getUsernameFromToken(jwtToken).equals(blog.getUser().getUsername())) {
            throw new BlogException("This blog does not belong to this user!");
        }
        blogRepository.save(blog);
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/{username}")
    public ResponseEntity getBlogs(@PathVariable("username") String username) {
        User user = userService.loadUserByUsername(username);

        return ResponseEntity.ok(blogRepository.findByUser(user));
    }

    @DeleteMapping
    public ResponseEntity deleteBlog(HttpServletRequest request, @RequestBody Blog deleteBlogRequest) throws Exception {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        Optional<Blog> opBlog = blogRepository.findById(deleteBlogRequest.getId());
        Blog blog = null;
        if (opBlog.isPresent()) {
            blog = opBlog.get();
        } else {
            throw new BlogException("Blog does not exist");
        }
        if (!jwtTokenUtil.getUsernameFromToken(jwtToken).equals(blog.getUser().getUsername())) {
            throw new BlogException("This blog does not belong to this user!");
        }
        blogRepository.delete(blog);
        return ResponseEntity.ok().build();
    }

}