package com.example.demo.controller;

import com.example.demo.exception.BlogException;
import com.example.demo.model.FollowRequest;
import com.example.demo.model.Following;
import com.example.demo.model.User;
import com.example.demo.repository.FollowingRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/following")
public class FollowingController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowingRepository followingRepository;

    @GetMapping
    public ResponseEntity getFollowersByLoggedInUser(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);

        User user = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));



        return ResponseEntity.ok(followingRepository.findByBaseUser(user));
    }

    @PostMapping
    public ResponseEntity follow(HttpServletRequest request, @RequestBody FollowRequest followRequest) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);

        User user = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));
        User userToBeFollowed = userService.loadUserByUsername(followRequest.getUsername());
        if (userToBeFollowed == null) {
            throw new BlogException("Username not found");
        }
        Following following = new Following();
        following.setBaseUser(user);
        following.setFollowing(followRequest.getUsername());
        followingRepository.save(following);

        return ResponseEntity.ok(followRequest.getUsername());
    }

    @DeleteMapping
    public ResponseEntity unFollow(HttpServletRequest request, @RequestBody FollowRequest followRequest) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);

        User user = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwtToken));
        User userToBeUnfollowed = userService.loadUserByUsername(followRequest.getUsername());


        long result =  followingRepository.deleteByBaseUserAndFollowing(user, userToBeUnfollowed.getUsername());

        return ResponseEntity.ok().build();
    }
}
