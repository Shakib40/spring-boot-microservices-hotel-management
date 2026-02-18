package com.auth.repository;

import com.auth.entity.blocked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedRepository extends JpaRepository<blocked, String> {
    Optional<blocked> findByUserName(String userName);

    void deleteByUserName(String userName);
}
