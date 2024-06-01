package com.github.javarushcommunity.jrtb.service;

import com.github.javarushcommunity.jrtb.dto.GroupStatDTO;
import com.github.javarushcommunity.jrtb.dto.StatisticDTO;
import com.github.javarushcommunity.jrtb.repository.entity.TelegramUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;


@Service
public class StatisticServiceImpl implements StatisticService {

    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    public StatisticServiceImpl(GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public StatisticDTO countBotStatistic() {
        List<GroupStatDTO> groupStatDTOs = groupSubService.findAll().stream()
                .filter(it -> !isEmpty(it.getUsers()))
                .map(groupSub -> new GroupStatDTO(groupSub.getId(), groupSub.getTitle(), groupSub.getUsers().size()))
                .toList();

        List<TelegramUser> allInActiveUsers = telegramUserService.findAllInActiveUsers();
        List<TelegramUser> allActiveUsers = telegramUserService.findAllActiveUsers();

        double groupsPerUser = getGroupsPerUser(allActiveUsers);

        return new StatisticDTO(allActiveUsers.size(), allInActiveUsers.size(), groupStatDTOs, groupsPerUser);
    }

    private double getGroupsPerUser(List<TelegramUser> allActiveUsers) {
        return allActiveUsers.stream()
                .mapToInt(it -> it.getGroupSubs().size()).sum() / allActiveUsers.size();
    }
}
