package com.user.repository;

import com.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
        Optional<User> findByUsername(String username);

        Optional<User> findByEmail(String email);

        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        @Query("""
                        SELECT u
                        FROM User u
                        WHERE (:role IS NULL OR u.role = :role)
                        """)
        Page<User> findUsersWithFilter(
                        @Param("role") com.user.enums.RoleEnum role,
                        Pageable pageable);
}
