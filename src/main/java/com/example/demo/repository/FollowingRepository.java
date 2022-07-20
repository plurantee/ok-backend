package com.example.demo.repository;

import com.example.demo.model.Following;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    @Transactional
    long deleteByBaseUserAndFollowing(User baseUser, String following);
    List<Following> findByBaseUser(User user);
}
