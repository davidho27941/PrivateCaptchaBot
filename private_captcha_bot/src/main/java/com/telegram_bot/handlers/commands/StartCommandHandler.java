package com.telegram_bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.telegram_bot.interfaces.GenericHandler;

public class StartCommandHandler implements GenericHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {
        
        long chat_id = update.getMessage().getChatId();

        SendMessage message = SendMessage
            .builder()
            .chatId(chat_id)
            .text("Hello")
            .build();

        telegramClient.execute(message);

    }
}