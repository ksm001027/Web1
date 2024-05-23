package com.example.web1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.web1.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserID(String userID);
}
