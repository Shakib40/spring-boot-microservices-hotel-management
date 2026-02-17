package com.auth.repository;

import com.auth.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserResponse, String> {
    Optional<UserResponse> findByUsername(String username);
}
