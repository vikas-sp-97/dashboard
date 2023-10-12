package com.example.dashboard.service;

import com.example.dashboard.entity.Role;
import com.example.dashboard.entity.UserEntity;
import com.example.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    public UserDetailsServiceImp(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        return new User(user.getUserName(), user.getPassword(), getAuthorizedRoleMap(user.getRoles()));
    }

    private Collection<GrantedAuthority> getAuthorizedRoleMap(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole().name())).collect(Collectors.toList());
    }
}
