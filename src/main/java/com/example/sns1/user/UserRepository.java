package com.example.sns1.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByUsername(String username);
}
