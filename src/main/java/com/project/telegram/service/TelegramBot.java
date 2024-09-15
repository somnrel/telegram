package com.project.telegram.service;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.project.telegram.config.BotConfig;
import com.project.telegram.entity.TelegramUser;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    UserService userService;

    @Autowired
    TelegramUserService telegramUserService;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Hi, " + name;
        sendMessage(chatId, answer);


    };


    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    private void sendContactRequest(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        message.setText("Здравствуйте. Для использования бота необходимо поделиться своим номером телефона");

        KeyboardButton button = new KeyboardButton("Поделиться контактом");
        button.setRequestContact(true);
        List<KeyboardRow> rows = new ArrayList<>();
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(button);
        rows.add(new KeyboardRow(buttons));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setKeyboard(rows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getAmount(long chatId, String phone) {
        return userService.getTransactionUserByPhone(phone);
    }


        @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            switch (command) {
                case  ("/start"):
                    sendContactRequest(update.getMessage().getChatId());
                    break;
                case ("/getAmount"):
                    if (telegramUserService.existsByChatId(update.getMessage().getChatId())) {
                        TelegramUser byChatId = telegramUserService.findByChatId(update.getMessage().getChatId());
                        Integer amount = getAmount(update.getMessage().getChatId(), byChatId.getPhone());
                        sendMessage(update.getMessage().getChatId(), "Ваш баланс бонусов составляет: " + amount);
                    } else {
                        sendMessage(update.getMessage().getChatId(), "Ваш номер не найден в базе клиентов спорткомплекса. Обратитесь к администратору");
                    }
                    break;
                default:
                    break;
            }
        } else if ((update.hasMessage() && update.getMessage().hasContact())) {
            String contactName = update.getMessage().getContact().getFirstName();
            String contactPhone = update.getMessage().getContact().getPhoneNumber();
            Long chatId = update.getMessage().getChatId();
            Boolean isUserExists = userService.getUserByPhone(contactPhone);
            if (isUserExists) {
                TelegramUser telegramUser = new TelegramUser();
                telegramUser.setName(contactName);
                telegramUser.setPhone(contactPhone);
                telegramUser.setChatId(chatId);
                telegramUserService.saveUser(telegramUser);
                sendMessage(update.getMessage().getChatId(), "Спасибо! Ваш контакт:\nИмя: " + contactName + "\nТелефон: " + contactPhone);
                sendMessage(update.getMessage().getChatId(), "Теперь вы будете получать уведомления о начислении и списании бонусов.");
            } else {
                sendMessage(update.getMessage().getChatId(), "Ваш номер не найден в базе клиентов спорткомплекса. Обратитесь к администратору");
            }
        }
    }
}
