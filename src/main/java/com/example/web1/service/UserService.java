package com.example.web1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.web1.model.User;
import com.example.web1.repository.UserRepository;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  public int login(String userID, String userPassword) {
    User user = userRepository.findByUserID(userID);
    if (user == null) {
      return -1; // 존재하지 않는 아이디
    } else if (!user.getUserPassword().equals(userPassword)) {
      return 0; // 비밀번호 불일치
    } else {
      return 1; // 로그인 성공
    }
  }

  @Override
  public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
    User user = userRepository.findByUserID(userID);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return new org.springframework.security.core.userdetails.User(user.getUserID(), user.getUserPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }
}
