package com.github.javarushcommunity.jrtb.command.annotation;

import java.lang.annotation.Retention;

import com.github.javarushcommunity.jrtb.command.Command;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark if {@link Command} can be viewed by admins.
 */
@Retention(RUNTIME)
public @interface AdminCommand {
}
