package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.command.annotation.AdminCommand;
import com.github.javarushcommunity.jrtb.dto.StatisticDTO;
import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import com.github.javarushcommunity.jrtb.service.StatisticService;
import com.github.javarushcommunity.jrtb.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.javarushcommunity.jrtb.command.CommandUtils.getChatId;
import static java.lang.String.format;

/**
 * Statistics {@link Command}
 */
@AdminCommand
public class StatCommand implements Command {

    private final StatisticService statisticService;
    private final SendBotMessageService sendBotMessageService;

    public final static String STAT_MESSAGE = "✨ <b>Подготовил статистику</b> ✨\n" +
            "- Количество активных пользователей: %s\n" +
            "- Количество неактивных пользователей: %s\n" +
            "- Среднее количество групп на одного пользователя: %s\n\n" +
            "<b>Информация по активным группам</b>:\n" +
            "%s";

    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService,
                        StatisticService statisticService) {
        this.sendBotMessageService = sendBotMessageService;
        this.statisticService = statisticService;
    }

    @Override
    public void execute(Update update) {
        StatisticDTO statisticDTO = statisticService.countBotStatistic();
        String collectedGroups = statisticDTO.getGroupStatDTOs().stream()
                        .map(it -> format("%s (id = %s) - %s подписчиков", it.getTitle(),
                                it.getId(), it.getActiveUserCount()))
                                .collect(Collectors.joining("\n"));

        sendBotMessageService.sendMessage(getChatId(update),
                format(STAT_MESSAGE, statisticDTO.getActiveUserCount(),
                        statisticDTO.getInactiveUserCount(),
                        statisticDTO.getAverageGroupCountByUser(),
                        collectedGroups));
    }
}
