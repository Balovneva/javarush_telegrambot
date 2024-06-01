package com.github.javarushcommunity.jrtb.service;

import com.github.javarushcommunity.jrtb.dto.GroupStatDTO;
import com.github.javarushcommunity.jrtb.dto.StatisticDTO;
import com.github.javarushcommunity.jrtb.repository.entity.GroupSub;
import com.github.javarushcommunity.jrtb.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for StatisticService")
public class StatisticServiceTest {
    private GroupSubService groupSubService;
    private TelegramUserService telegramUserService;

    private StatisticService statisticService;

    @BeforeEach
    public void init() {
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        statisticService = new StatisticServiceImpl(groupSubService, telegramUserService);
    }

    @Test
    public void shouldProperlySendStatDTO() {
        //given
        Mockito.when(telegramUserService.findAllInActiveUsers()).thenReturn(singletonList(new TelegramUser()));
        TelegramUser activeUser = new TelegramUser();
        activeUser.setGroupSubs(singletonList(new GroupSub()));

        Mockito.when(telegramUserService.findAllActiveUsers()).thenReturn(singletonList(activeUser));
        GroupSub groupSub = new GroupSub();
        groupSub.setTitle("group");
        groupSub.setId(1);
        groupSub.setUsers(singletonList(new TelegramUser()));
        Mockito.when(groupSubService.findAll()).thenReturn(singletonList(groupSub));

        //when
        StatisticDTO statisticDTO = statisticService.countBotStatistic();

        //then
        Assertions.assertNotNull(statisticDTO);
        Assertions.assertEquals(1, statisticDTO.getActiveUserCount());
        Assertions.assertEquals(1, statisticDTO.getInactiveUserCount());
        Assertions.assertEquals(1.0, statisticDTO.getAverageGroupCountByUser());
        Assertions.assertEquals(singletonList(new GroupStatDTO(groupSub.getId(), groupSub.getTitle(),
                groupSub.getUsers().size())), statisticDTO.getGroupStatDTOs());
    }
}
