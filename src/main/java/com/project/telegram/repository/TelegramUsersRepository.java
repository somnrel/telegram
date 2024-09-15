package com.project.telegram.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.telegram.entity.TelegramUser;

public interface TelegramUsersRepository extends JpaRepository<TelegramUser, Integer> {

    boolean existsByChatId(long chatId);

    Optional<TelegramUser> findByPhone(String phone);

    Optional<TelegramUser> findByChatId(long integer);
}
