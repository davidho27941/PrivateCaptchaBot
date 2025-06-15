package com.telegram_bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.telegram_bot.interfaces.GenericHandler;

public class CommandHandler implements GenericHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {}

    public void sendMessage(Update update, TelegramClient telegramClient, long chat_id, String text) throws TelegramApiException {
      
        SendMessage message = SendMessage
            .builder()
            .chatId(chat_id)
            .text(text)
            .parseMode("Markdown")
            .build();

        telegramClient.execute(message);
    }

    public void sendMessage(Update update, TelegramClient telegramClient, long chat_id, String text, int message_id) throws TelegramApiException {
      
        SendMessage message = SendMessage
            .builder()
            .chatId(chat_id)
            .text(text)
            .parseMode("Markdown")
            .replyToMessageId(message_id)
            .build();

        telegramClient.execute(message);
    }

}