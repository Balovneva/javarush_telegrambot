package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import com.github.javarushcommunity.jrtb.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.javarushcommunity.jrtb.command.CommandUtils.getChatId;

public class StopCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public static final String STOP_MESSAGE = "Дективировал все ваши подписки \uD83D\uDE1F.";

    public StopCommand(SendBotMessageService sendBotMessageService,
                       TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        telegramUserService.findByChatId(chatId).ifPresent(
                user -> {
                    user.setActive(false);
                    telegramUserService.save(user);
                });

        sendBotMessageService.sendMessage(chatId, STOP_MESSAGE);
    }
}
