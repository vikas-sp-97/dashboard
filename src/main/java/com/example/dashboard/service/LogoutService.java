package com.example.dashboard.service;

import com.example.dashboard.entity.Token;
import com.example.dashboard.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    @Autowired
    TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (!(StringUtils.hasText(authHeader) ||
                        StringUtils.startsWithIgnoreCase(authHeader, "Bearer "))) {
            return;
        }
        String token = authHeader.substring(7);
        Optional<Token> tokenFromDb = tokenRepository.findByToken(token);
        if(tokenFromDb.isPresent()){
            tokenFromDb.get().setExpired(true);
            tokenFromDb.get().setRevoked(true);

            tokenRepository.save(tokenFromDb.get());
        }
    }
}
