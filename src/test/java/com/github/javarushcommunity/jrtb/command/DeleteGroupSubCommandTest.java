package com.github.javarushcommunity.jrtb.command;

import com.github.javarushcommunity.jrtb.repository.entity.GroupSub;
import com.github.javarushcommunity.jrtb.repository.entity.TelegramUser;
import com.github.javarushcommunity.jrtb.service.GroupSubService;
import com.github.javarushcommunity.jrtb.service.SendBotMessageService;
import com.github.javarushcommunity.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.javarushcommunity.jrtb.command.AbstractCommandTest.prepareUpdate;
import static com.github.javarushcommunity.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for DeleteGroupSubCommand")
public class DeleteGroupSubCommandTest {

    private Command command;
    private TelegramUserService telegramUserService;
    private SendBotMessageService sendBotMessageService;
    private GroupSubService groupSubService;

    @BeforeEach
    public void init() {
        telegramUserService = Mockito.mock(TelegramUserService.class);
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);

        command = new DeleteGroupSubCommand(telegramUserService,
                sendBotMessageService, groupSubService);
    }

    @Test
    public void shouldProperlyReturnEmptySubscriptionList() {
        //given
        String chatId = "23456";
        Update update = prepareUpdate(Long.valueOf(chatId), DELETE_GROUP_SUB.getCommandName());

        Mockito.when(telegramUserService.findByChatId(chatId))
                .thenReturn(Optional.of(new TelegramUser()));

        String exceptedMessage = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addgroupsub";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId, exceptedMessage);
    }

    @Test
    public void shouldProperlyReturnSubscriptionList() {
        //given
        String chatId = "23456";
        Update update = prepareUpdate(Long.valueOf(chatId), DELETE_GROUP_SUB.getCommandName());
        TelegramUser telegramUser = new TelegramUser();
        GroupSub gs1 = new GroupSub();
        gs1.setId(123);
        gs1.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(gs1));
        Mockito.when(telegramUserService.findByChatId(chatId))
                .thenReturn(Optional.of(telegramUser));
        String exceptedMessage = "Чтобы удалить подписку на группу передай команду вместе с ID группы.\n" +
                "Например: /deleteGroupSub 16\n\n" +
                "Я подготовил список групп, на которые ты подписан \n\n" +
                "имя группы - ID группы \n\n" +
                "GS1 Title - 123 \n";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId, exceptedMessage);
    }

    @Test
    public void shouldRejectByInvalidGroupId() {
        //given
        String chatId = "2345";
        Update update = prepareUpdate(Long.valueOf(chatId), format("%s %s",
                DELETE_GROUP_SUB.getCommandName(), "GroupSubId"));
        TelegramUser telegramUser = new TelegramUser();
        GroupSub gs1 = new GroupSub();
        gs1.setId(123);
        gs1.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(gs1));
        Mockito.when(telegramUserService.findByChatId(chatId))
                .thenReturn(Optional.of(telegramUser));
        String exceptedMessage = "Неправильный формат ID группы.\n" +
                "ID должно быть целым положительным числом.";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId, exceptedMessage);
    }

    @Test
    public void ShouldProperlyDeleteByGroupId() {
        String chatId = "2345";
        String groupId = "123";
        Update update = prepareUpdate(Long.valueOf(chatId), format("%s %s",
                DELETE_GROUP_SUB.getCommandName(), groupId));

        GroupSub gs1 = new GroupSub();
        gs1.setId(123);
        gs1.setTitle("GS1 Title");
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId);
        telegramUser.setGroupSubs(singletonList(gs1));
        ArrayList<TelegramUser> users = new ArrayList<>();
        users.add(telegramUser);
        gs1.setUsers(users);
        Mockito.when(groupSubService.findById(Integer.valueOf(groupId)))
                .thenReturn(Optional.of(gs1));
        Mockito.when(telegramUserService.findByChatId(chatId))
                .thenReturn(Optional.of(telegramUser));

        String exceptedMessage = "Удалил подписку на группу: GS1 Title";

        //when
        command.execute(update);

        //then
        users.remove(telegramUser);
        Mockito.verify(groupSubService).save(gs1);
        Mockito.verify(sendBotMessageService).sendMessage(chatId, exceptedMessage);
    }

    @Test
    public void ShouldDoesNotExistByGroupId() {
        //given
        String chatId = "2345";
        Integer groupId = 124;
        Update update = prepareUpdate(Long.valueOf(chatId), format("%s %s",
                DELETE_GROUP_SUB.getCommandName(), groupId));

        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());

        String exceptedMessage = "Не нашел такой группы";

        //when
        command.execute(update);
        
        //then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId, exceptedMessage);
    }
}