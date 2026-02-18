package com.auth.serviceimp;

import com.auth.entity.blocked;
import com.auth.repository.BlockedRepository;
import com.auth.service.BlockedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockedServiceImpl implements BlockedService {

    private final BlockedRepository blockedRepository;

    @Override
    public blocked blockUser(blocked blockedUser) {
        return blockedRepository.save(blockedUser);
    }

    @Override
    public Optional<blocked> getBlockedUserById(String id) {
        return blockedRepository.findById(id);
    }

    @Override
    public Optional<blocked> getBlockedUserByUserName(String userName) {
        return blockedRepository.findByUserName(userName);
    }

    @Override
    public List<blocked> getAllBlockedUsers() {
        return blockedRepository.findAll();
    }

    @Override
    public void unblockUser(String id) {
        blockedRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void unblockUserByUserName(String userName) {
        blockedRepository.deleteByUserName(userName);
    }
}
