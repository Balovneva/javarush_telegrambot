package com.github.javarushcommunity.jrtb.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.javarushcommunity.jrtb.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level test for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest{
    @Override
    String getCommandName() {
        return "/ffffd";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
