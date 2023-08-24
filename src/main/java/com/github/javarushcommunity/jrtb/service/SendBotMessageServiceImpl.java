package com.github.javarushcommunity.jrtb.service;

import com.github.javarushcommunity.jrtb.bot.JavaRushBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final JavaRushBot javaRushBot;

    @Autowired
    public SendBotMessageServiceImpl(JavaRushBot javaRushBot) {
        this.javaRushBot = javaRushBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        System.out.println(chatId);
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            javaRushBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project
            e.printStackTrace();
        }
    }
}
