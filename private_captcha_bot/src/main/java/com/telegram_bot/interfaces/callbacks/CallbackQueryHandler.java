package com.telegram_bot.interfaces.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/** Generic interface for processing callback queries. */
public interface CallbackQueryHandler {
    void handle(Update update, TelegramClient telegramClient) throws TelegramApiException;
}
