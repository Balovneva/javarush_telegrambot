package com.github.javarushcommunity.jrtb.service;

import com.github.javarushcommunity.jrtb.javarushclient.JavaRushPostClient;
import com.github.javarushcommunity.jrtb.javarushclient.dto.PostInfo;
import com.github.javarushcommunity.jrtb.repository.entity.GroupSub;
import com.github.javarushcommunity.jrtb.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewArticleServiceImpl implements FindNewArticleService {
    public static final String JAVA_RUSH_WEB_POST_FORMAT = "https://javarush.com/groups/posts/%s";

    public final JavaRushPostClient javaRushPostClient;
    public final GroupSubService groupSubService;
    public final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewArticleServiceImpl(JavaRushPostClient javaRushPostClient, GroupSubService groupSubService,
                                     SendBotMessageService sendBotMessageService) {
        this.javaRushPostClient = javaRushPostClient;
        this.groupSubService = groupSubService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewArticles() {
        groupSubService.findAll().forEach(gSub -> {
            List<PostInfo> newPosts = javaRushPostClient.findNewPosts(gSub.getId(), gSub.getLastArticleId());

            setNewLastArticleId(gSub, newPosts);

            notifySubscribersAboutNewArticles(gSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewArticles(GroupSub gSub, List<PostInfo> newPosts) {
        Collections.reverse(newPosts);
        List<String> messageWithNewArticles = newPosts.stream()
                .map(post -> String.format("✨ Вышла новая статья <b>%s</b> в группе <b>%s</b>.✨\n\n" +
                                "<b>Описание:</b> %s\n\n" +
                                "<b>Ссылка:</b> %s\n", post.getTitle(), gSub.getTitle(),
                        post.getDescription(), getPostUrl(post.getKey()))).collect(Collectors.toList());

        gSub.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messageWithNewArticles));
    }

    private void setNewLastArticleId(GroupSub gSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    gSub.setLastArticleId(id);
                    groupSubService.save(gSub);
                });
    }

    private String getPostUrl(String key) {
        return String.format(JAVA_RUSH_WEB_POST_FORMAT, key);
    }
}


