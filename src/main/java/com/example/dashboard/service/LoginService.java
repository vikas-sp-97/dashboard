package com.example.dashboard.service;

import com.example.dashboard.dto.LoginDTO;
import com.example.dashboard.dto.RegisterDTO;
import com.example.dashboard.dto.AuthResponseDTO;
import com.example.dashboard.dto.ClientUserDTO;
import com.example.dashboard.entity.Role;
import com.example.dashboard.entity.Token;
import com.example.dashboard.entity.UserEntity;
import com.example.dashboard.enums.ActiveStatus;
import com.example.dashboard.enums.RoleEnum;
import com.example.dashboard.enums.TokenType;
import com.example.dashboard.repository.RoleRepository;
import com.example.dashboard.repository.TokenRepository;
import com.example.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    TokenRepository tokenRepository;

    public ResponseEntity<?> registerUser(RegisterDTO registerDTO) {
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
        var token = jwtService.generateToken(user);

        addToken(user, token);

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(token));

    }

    private void addToken(UserEntity user, String token) {
        Token userToken = Token.builder()
                .token(token)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .userEntity(user)
                .build();

        tokenRepository.save(userToken);
    }

    public ResponseEntity<AuthResponseDTO> loginUser(LoginDTO loginDTO) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword()));

//        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByUserName(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var token = jwtService.generateToken(user);

        System.out.println("token:  ---- \n"+token+" ---- \n");
        System.out.println(user.getId());

        revokeAccessTokens(user);
        
        addToken(user, token);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(token));
    }

    public void revokeAccessTokens(UserEntity user){
        var allValidToken = tokenRepository.findAllTokenByUserId(user.getId());

        if(!allValidToken.isEmpty()) {
            List<Token> updatedTokens = new ArrayList<>();
            for (Token t : allValidToken) {
                t.setExpired(true);
                t.setRevoked(true);
                updatedTokens.add(t);
            }

            tokenRepository.saveAll(updatedTokens);
        }
//        System.out.println(" ---- \n"+allValidToken+" ---- \n");
    }

    public ResponseEntity<String> registerClientUser(ClientUserDTO clientUserDTO) {
        if(userRepository.existsByUserName(clientUserDTO.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists!");
        }

        UserEntity user = new UserEntity();
        user.setUserName(clientUserDTO.getUsername());
        System.out.println("----------\n");
        Role role = roleRepository.findByRole(RoleEnum.CLIENT_USER).get();
        System.out.println(role);
        user.setRoles(Collections.singletonList(role));
        if(clientUserDTO.getActiveStatus() != null){
            if(clientUserDTO.getActiveStatus().toString().equalsIgnoreCase(ActiveStatus.ACTIVE.name()))
                user.setActiveStatus(ActiveStatus.ACTIVE);
        }else{
            user.setActiveStatus(ActiveStatus.INACTIVE);
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Client user registered successfully!");

    }
}
