package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.dto.GroupStatDTO;
import com.github.javarushcommunity.jrtb.dto.StatisticDTO;
import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import com.github.javarushcommunity.jrtb.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.github.javarushcommunity.jrtb.command.AbstractCommandTest.prepareUpdate;
import static com.github.javarushcommunity.jrtb.command.StatCommand.STAT_MESSAGE;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class StatCommandTest {

    private SendBotMessageService sendBotMessageService;
    private StatisticService statisticService;
    private Command statCommand;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        statisticService = Mockito.mock(StatisticService.class);
        statCommand = new StatCommand(sendBotMessageService, statisticService);
    }

    @Test
    public void shouldProperlySendMessage() {
        //given
        Long chatId = 123456L;
        GroupStatDTO groupStatDTO = new GroupStatDTO(1, "group", 1);
        StatisticDTO statisticDTO = new StatisticDTO(1, 1,
                singletonList(groupStatDTO), 2.5);
        Mockito.when(statisticService.countBotStatistic())
                .thenReturn(statisticDTO);

        //when
        statCommand.execute(prepareUpdate(chatId, CommandName.STAT.getCommandName()));

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(),
                format(STAT_MESSAGE, statisticDTO.getActiveUserCount(),
                        statisticDTO.getInactiveUserCount(),
                        statisticDTO.getAverageGroupCountByUser(),
                        format("%s (id = %s) - %s подписчиков", groupStatDTO.getTitle(),
                                groupStatDTO.getId(), groupStatDTO.getActiveUserCount())));
    }
}
