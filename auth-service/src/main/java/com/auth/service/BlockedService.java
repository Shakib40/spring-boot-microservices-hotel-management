package com.auth.service;

import com.auth.entity.blocked;
import java.util.List;
import java.util.Optional;

public interface BlockedService {
    blocked blockUser(blocked blockedUser);

    Optional<blocked> getBlockedUserById(String id);

    Optional<blocked> getBlockedUserByUserName(String userName);

    List<blocked> getAllBlockedUsers();

    void unblockUser(String id);

    void unblockUserByUserName(String userName);
}
