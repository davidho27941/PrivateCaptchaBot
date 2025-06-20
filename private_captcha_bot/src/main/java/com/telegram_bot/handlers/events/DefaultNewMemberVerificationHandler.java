package com.telegram_bot.handlers.events;

import com.telegram_bot.interfaces.events.NewMemberVerificationHandler;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/** Default implementation that greets new users and restricts them until verification. */
public class DefaultNewMemberVerificationHandler implements NewMemberVerificationHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {
        if (!update.hasMessage() || update.getMessage().getNewChatMembers().isEmpty()) {
            return;
        }

        User user = update.getMessage().getNewChatMembers().get(0);
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        long chatId = update.getMessage().getChatId();
        long userId = user.getId();

        SendMessage message =
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Hello " + firstName + " " + lastName + " (" + userName + "). Are you a human?")
                        .replyMarkup(
                                InlineKeyboardMarkup.builder()
                                        .keyboardRow(
                                                new InlineKeyboardRow(
                                                        InlineKeyboardButton.builder()
                                                                .text("Click to confirm")
                                                                .callbackData("Catcha_pass")
                                                                .build()))
                                        .build())
                        .build();

        ChatPermissions permission = ChatPermissions.builder().canSendMessages(false).build();
        RestrictChatMember restriction =
                RestrictChatMember.builder()
                        .chatId(chatId)
                        .userId(userId)
                        .permissions(permission)
                        .build();

        telegramClient.execute(message);
        telegramClient.execute(restriction);
    }
}
