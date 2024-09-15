package com.project.telegram.controller;

import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.telegram.entity.TelegramUser;
import com.project.telegram.service.TelegramBot;
import com.project.telegram.service.TelegramUserService;

@Controller
public class TelegramNotificationController {

    @Autowired
    TelegramBot telegramBot;

    @Autowired
    TelegramUserService telegramUserService;

    @PostMapping("sendAmount")
    public void sendAmount(@RequestBody Map<String, String> body) {
        long amount = Long.parseLong(body.get("amount"));
        String phone = body.get("phone");


        TelegramUser user = telegramUserService.findByPhone(phone);
        if (Objects.nonNull(user)) {
            telegramBot.sendMessage(user.getChatId(), amount > 0 ? "Вам начислено: " + amount + " бонусов" : "У Вас списано: " + amount + " бонусов.");
        }

    }

}
