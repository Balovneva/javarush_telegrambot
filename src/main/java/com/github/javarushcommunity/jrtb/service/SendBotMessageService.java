package com.github.javarushcommunity.jrtb.service;

import java.util.List;

/**
 * Service for sending messages via telegram-bot.
 */
public interface SendBotMessageService {
    /**
     * Send message via telegram-bot.
     *
     * @param chatId provided chatId in which would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(Long chatId, String message);
    /**
     * Send message via telegram-bot.
     *
     * @param chatId provided chatId in which would be sent.
     * @param messages message collection of provided messages to be sent.
     */
    void sendMessage(Long chatId, List<String> messages);
}
