package com.telegram_bot;

import com.telegram_bot.handlers.callbacks.CaptchaPassCallbackHandler;
import com.telegram_bot.handlers.commands.BanUserCommandHandler;
import com.telegram_bot.handlers.commands.CommandHandler;
import com.telegram_bot.handlers.commands.RestrictUserCommandHandler;
import com.telegram_bot.handlers.commands.UnBanUserCommandHandler;
import com.telegram_bot.handlers.events.DefaultNewMemberVerificationHandler;
import com.telegram_bot.interfaces.callbacks.CallbackQueryHandler;
import com.telegram_bot.interfaces.events.NewMemberVerificationHandler;
import java.util.HashMap;
import java.util.Map;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class PrivateCatchaBot implements LongPollingSingleThreadUpdateConsumer {
  private final TelegramClient telegramClient;

  private final Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
  private final Map<String, CallbackQueryHandler> callbackQueryHandlerMap = new HashMap<>();
  private final NewMemberVerificationHandler newMemberHandler = new DefaultNewMemberVerificationHandler();

  public PrivateCatchaBot(String botToken) {
    telegramClient = new OkHttpTelegramClient(botToken);

    commandHandlerMap.put("/ban", new BanUserCommandHandler());
    commandHandlerMap.put("/unban", new UnBanUserCommandHandler());
    commandHandlerMap.put("/restrict", new RestrictUserCommandHandler());

    callbackQueryHandlerMap.put("Catcha_pass", new CaptchaPassCallbackHandler());
  }

  @Override
  public void consume(Update update) {

    if (update.hasMessage() && !update.getMessage().getNewChatMembers().isEmpty()) {
      try {
        newMemberHandler.handle(update, telegramClient);
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }

    } else if (update.hasMessage() && update.getMessage().isCommand()) {

      String text = update.getMessage().getText().trim();
      String[] parted_text = text.split("\\s+", 2);
      String commandKey = parted_text[0];

      CommandHandler handler = commandHandlerMap.get(commandKey);

      if (handler != null) {
        try {
          handler.handle(update, telegramClient);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
      } else {
        long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder().chatId(chatId).text("Unknown command").build();

        try {
          telegramClient.execute(message);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
      }

    } else if (update.hasCallbackQuery()) {

      String data = update.getCallbackQuery().getData();
      CallbackQueryHandler handler = callbackQueryHandlerMap.get(data);
      if (handler != null) {
        try {
          handler.handle(update, telegramClient);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
