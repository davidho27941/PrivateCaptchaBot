package com.telegram_bot.interfaces.events;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/** Interface for handling new member join events that require verification. */
public interface NewMemberVerificationHandler {
    void handle(Update update, TelegramClient telegramClient) throws TelegramApiException;
}
