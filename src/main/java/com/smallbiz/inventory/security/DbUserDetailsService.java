package com.smallbiz.inventory.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smallbiz.inventory.repo.UserAccountRepository;

@Service
public class DbUserDetailsService implements UserDetailsService {

  private final UserAccountRepository users;

  public DbUserDetailsService(UserAccountRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    var u = users.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new User(
      u.getUsername(),
      u.getPassword(),   // FIXED HERE
      List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
    );
  }
}