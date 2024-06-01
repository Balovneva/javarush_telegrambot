package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.repository.entity.TelegramUser;
import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import com.github.javarushcommunity.jrtb.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.javarushcommunity.jrtb.command.CommandName.ADD_GROUP_SUB;
import static com.github.javarushcommunity.jrtb.command.CommandName.HELP;
import static com.github.javarushcommunity.jrtb.command.CommandUtils.getChatId;

public class StartCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public static final String START_MESSAGE = String.format("Привет. Я бот.\n" +
                    "Я помогу тебе быть в курсе последних статей тех авторов,\n" +
                    "которые тебе интересны.\n\n" +
                    "Нажимай %s чтобы подписаться на группу статей.\n" +
                    "Не знаешь о чем я? Напиши %s,чтобы узнать что я умею.",
            ADD_GROUP_SUB.getCommandName(), HELP.getCommandName());

    public StartCommand(SendBotMessageService sendBotMessageService,
                        TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        telegramUserService.findByChatId(chatId).ifPresentOrElse(
                user -> {
                    user.setActive(true);
                    telegramUserService.save(user);
                },
                () -> {
                    TelegramUser user = new TelegramUser();
                    user.setChatId(chatId);
                    user.setActive(true);
                    telegramUserService.save(user);
                }
        );

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }
}


