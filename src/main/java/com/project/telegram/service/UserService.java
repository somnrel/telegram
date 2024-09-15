package com.project.telegram.service;


import org.springframework.stereotype.Service;
import com.project.telegram.client.UserClient;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public Boolean getUserByPhone(String phone) {
        return userClient.getUserByPhone(phone);
    }

    public Integer getTransactionUserByPhone(String phone) {
        return userClient.getTransactionUserByPhone(phone);
    }
}