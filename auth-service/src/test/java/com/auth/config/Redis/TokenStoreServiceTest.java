package com.auth.config.Redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TokenStoreServiceTest {

    private TokenStoreService tokenStoreService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        tokenStoreService = new TokenStoreService(redisTemplate);
    }

    @Test
    void getAttemptCount_ShouldReturnZero_WhenValueIsNull() {
        // Arrange
        String username = "testuser";
        when(valueOperations.get("attempt:" + username)).thenReturn(null);

        // Act
        int count = tokenStoreService.getAttemptCount(username);

        // Assert
        assertEquals(0, count);
    }

    @Test
    void getAttemptCount_ShouldReturnCorrectCount_WhenValueIsPresent() {
        // Arrange
        String username = "testuser";
        when(valueOperations.get("attempt:" + username)).thenReturn(5);

        // Act
        int count = tokenStoreService.getAttemptCount(username);

        // Assert
        assertEquals(5, count);
    }
}
