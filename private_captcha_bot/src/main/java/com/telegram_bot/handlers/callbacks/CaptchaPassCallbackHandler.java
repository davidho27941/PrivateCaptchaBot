package com.telegram_bot.handlers.callbacks;

import static java.lang.Math.toIntExact;

import com.telegram_bot.interfaces.callbacks.CallbackQueryHandler;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/** Handles the "Catcha_pass" callback query to verify a user. */
public class CaptchaPassCallbackHandler implements CallbackQueryHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {
        if (!update.hasCallbackQuery()) {
            return;
        }

        String data = update.getCallbackQuery().getData();
        if (!"Catcha_pass".equals(data)) {
            return;
        }

        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        User from = update.getCallbackQuery().getFrom();
        long userId = from.getId();

        EditMessageText newMessage =
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(toIntExact(messageId))
                        .text("Thanks for your cooperation.")
                        .build();
        telegramClient.execute(newMessage);

        ChatPermissions permission = ChatPermissions.builder().canSendMessages(true).build();
        RestrictChatMember release =
                RestrictChatMember.builder()
                        .chatId(chatId)
                        .userId(userId)
                        .permissions(permission)
                        .build();
        telegramClient.execute(release);
    }
}
