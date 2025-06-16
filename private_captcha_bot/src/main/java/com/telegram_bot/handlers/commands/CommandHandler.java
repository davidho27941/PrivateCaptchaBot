package com.telegram_bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.telegram_bot.interfaces.GenericHandler;

import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Model.CommandSpec;

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

    public void showHelpMessage(Update update, TelegramClient telegramClient, CommandLine cmd, long chat_id, int message_id) throws TelegramApiException {

        CommandSpec spec = cmd.getCommandSpec();
        spec.usageMessage().width(80);
        spec.usageMessage().longOptionsMaxWidth(30);

        StringWriter sw = new StringWriter();
        cmd.usage(new PrintWriter(sw), Ansi.OFF);
        String usageMessage = sw.toString();

        sendMessage(update, telegramClient, chat_id, usageMessage, message_id);
    }

}