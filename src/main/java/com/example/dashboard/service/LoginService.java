package com.example.dashboard.service;

import com.example.dashboard.LoginDTO;
import com.example.dashboard.RegisterDTO;
import com.example.dashboard.entity.Role;
import com.example.dashboard.entity.UserEntity;
import com.example.dashboard.enums.ActiveStatus;
import com.example.dashboard.enums.RoleEnum;
import com.example.dashboard.repository.RoleRepository;
import com.example.dashboard.repository.UserRepository;
import com.example.dashboard.security.DashboardSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class LoginService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public ResponseEntity<String> registerUser(RegisterDTO registerDTO) {
        if(userRepository.existsByUserName(registerDTO.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists!");
        }

        UserEntity user = new UserEntity();
        user.setUserName(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Role role = roleRepository.findByRole(RoleEnum.USER).get();
        user.setRoles(Collections.singletonList(role));
        user.setActiveStatus(ActiveStatus.ACTIVE);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");

    }

    public ResponseEntity<String> loginUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully!");
    }
}
