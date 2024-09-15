package com.project.telegram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.telegram.entity.TelegramUser;
import com.project.telegram.repository.TelegramUsersRepository;

@Service
public class TelegramUserService {

    @Autowired
    private TelegramUsersRepository repository;

    public void saveUser(TelegramUser telegramUser) {
        repository.save(telegramUser);
    }

    public boolean existsByChatId(long chatId) {
        return repository.existsByChatId(chatId);
    }

    public TelegramUser findByPhone(String phone) {
        return repository.findByPhone(phone).orElse(null);
    }

    public TelegramUser findByChatId(long chatId) {
        return repository.findByChatId(chatId).orElse(null);
    }
}
