package com.example.dashboard.repository;

import com.example.dashboard.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t from Token t inner join UserEntity u on t.userEntity.id = u.id
            where u.id = :userId and (t.expired = false and t.revoked = false)
            """)
    List<Token> findAllTokenByUserId(Integer userId);

    Optional<Token> findByToken(String token);
}
