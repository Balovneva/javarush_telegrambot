package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.javarushcommunity.jrtb.command.CommandName.*;
import static com.github.javarushcommunity.jrtb.command.CommandUtils.*;

public class HelpCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Доступные команды</b>✨ \n\n " +
                    "<b>Начать\\закончить работу с ботом</b>\n" +
                    "%s - начать работу со мной\n" +
                    "%s - приостановить работу со мной\n\n" +

                    "Работа с подписками на группы:\n" +
                    "%s - подписаться на группу статей\n" +
                    "%s - получить список групп, на которые подписан\n\n" +

                    "%s - получить помощь в работе со мной\n" +
                    "%s - получить мою статистику использования\n",
            START.getCommandName(), STOP.getCommandName(), ADD_GROUP_SUB.getCommandName(),
            LIST_GROUP_SUB.getCommandName(), HELP.getCommandName(), STAT.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(getChatId(update), HELP_MESSAGE);
    }
}
