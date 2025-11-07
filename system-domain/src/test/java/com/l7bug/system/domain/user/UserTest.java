package com.l7bug.system.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("root");
        user.setPassword("root");
        user.setNickname("root");
        user.setStatus(Status.ENABLE);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isEnable() {
        boolean enable = user.isEnable();
        Assertions.assertTrue(enable);
    }

    @Test
    void isDisable() {
        boolean disable = user.isDisable();
        Assertions.assertFalse(disable);
    }

    @Test
    void setDisable() {
        user.setDisable();
        Assertions.assertTrue(user.isDisable());
    }

    @Test
    void setEnable() {
        user.setDisable();
        Assertions.assertFalse(user.isEnable());
        user.setEnable();
        Assertions.assertTrue(user.isEnable());
    }


    @Test
    void saveTest(){
        UserGateway mock = Mockito.mock(UserGateway.class);
        user.save(mock);
    }
}