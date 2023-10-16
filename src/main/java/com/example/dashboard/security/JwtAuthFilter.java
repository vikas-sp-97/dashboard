package com.example.dashboard.security;

import com.example.dashboard.entity.Token;
import com.example.dashboard.repository.TokenRepository;
import com.example.dashboard.service.JwtService;
import com.example.dashboard.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsServiceImp userDetailsImp;

    @Autowired
    TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (!(StringUtils.hasText(authHeader) ||
                StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")))
        {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            String token = authHeader.substring(7);
            String username = jwtService.extractUserName(token);
            UserDetails userDetails = userDetailsImp.userDetailsService().loadUserByUsername(username);

            Optional<Token> tokenFromDb = tokenRepository.findByToken(token);


            if(StringUtils.hasText(token) && jwtService.isTokenValid(token, userDetails)
                && !(tokenFromDb.get().isExpired() && tokenFromDb.get().isRevoked())){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.FORBIDDEN);
        }
    }
}
