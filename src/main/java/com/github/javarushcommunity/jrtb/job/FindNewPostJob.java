package com.github.javarushcommunity.jrtb.job;

import com.github.javarushcommunity.jrtb.service.FindNewPostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Job for finding new posts.
 */
@Slf4j
@Component
public class FindNewPostJob {

    private final FindNewPostsService findNewPostsService;

    public FindNewPostJob(FindNewPostsService findNewPostsService) {
        this.findNewPostsService = findNewPostsService;
    }

    @Scheduled(fixedRateString = "${bot.recountNewPostFixedRate}")
    public void findNewPosts() {
        LocalDateTime start = LocalDateTime.now();

        log.info("Find new posts job started.");

        findNewPostsService.findNewPosts();

        LocalDateTime end = LocalDateTime.now();

        log.info("Find new posts job finished. Took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC));
    }

    
}
