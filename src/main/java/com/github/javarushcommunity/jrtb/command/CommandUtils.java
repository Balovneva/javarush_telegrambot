package com.github.javarushcommunity.jrtb.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Utils class for Commands.
 */
public class CommandUtils {

    /**
     * Get chatId from {@link Update} object.
     *
     * @param update provided {@link Update}
     * @return chatId from provided {@link Update} object.
     */
    public static String getChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    /**
     * Get message from {@link Update} object.
     *
     * @param update provided {@link Update}
     * @return the text of the message provided {@link Update} object.
     */
    public static String getMessage(Update update) {
        return update.getMessage().getText();
    }
}
