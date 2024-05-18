package com.example.web1.service;

import com.example.web1.model.User;
import com.example.web1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUserID(userID);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return org.springframework.security.core.userdetails.User.withUsername(user.getUserID())
      .password(user.getPassword())
      .roles("USER") // 필요한 경우 사용자 역할 추가
      .build();
  }

  public void saveUser(String userID, String password) {
    User user = new User();
    user.setUserID(userID);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }
}
