package com.telegram_bot.handlers.commands;

import com.telegram_bot.interfaces.GenericHandler;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi;

public class CommandHandler implements GenericHandler {

  @Override
  public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {}

  public void sendMessage(Update update, TelegramClient telegramClient, long chat_id, String text)
      throws TelegramApiException {

    SendMessage message =
        SendMessage.builder().chatId(chat_id).text(text).parseMode("Markdown").build();

    telegramClient.execute(message);
  }

  public void sendMessage(
      Update update, TelegramClient telegramClient, long chat_id, String text, int message_id)
      throws TelegramApiException {

    SendMessage message =
        SendMessage.builder()
            .chatId(chat_id)
            .text(text)
            .parseMode("Markdown")
            .replyToMessageId(message_id)
            .build();

    telegramClient.execute(message);
  }

  public void showHelpMessage(
      Update update, TelegramClient telegramClient, CommandLine cmd, long chat_id, int message_id)
      throws TelegramApiException {

    StringWriter sw = new StringWriter();
    cmd.usage(new PrintWriter(sw), Ansi.OFF);
    String usageMessage = sw.toString();

    String markdownBlock2 = String.format("```%n%s%n```", usageMessage);

    sendMessage(update, telegramClient, chat_id, markdownBlock2, message_id);
  }

  public Long extractUserId(Update update, List<String> params, int index) {
    if (params != null && params.size() > index) {
      try {
        return Long.parseLong(params.get(index));
      } catch (NumberFormatException e) {
        return null;
      }
    }
    if (update.getMessage().getReplyToMessage() != null) {
      return update.getMessage().getReplyToMessage().getFrom().getId();
    }
    return null;
  }
}
