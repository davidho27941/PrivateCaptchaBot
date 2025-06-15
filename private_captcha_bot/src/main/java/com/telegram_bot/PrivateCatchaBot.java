package com.telegram_bot;

import java.util.List;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;

import static java.lang.Math.toIntExact;

import com.telegram_bot.handlers.commands.BanUserCommandHandler;
import com.telegram_bot.handlers.commands.StartCommandHandler;
import com.telegram_bot.handlers.commands.UnBanUserCommandHandler;

public class PrivateCatchaBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public PrivateCatchaBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && !update.getMessage().getNewChatMembers().isEmpty()) {
            User user = update.getMessage().getNewChatMembers().get(0);
            
            String userName = user.getUserName();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            
            long chat_id = update.getMessage().getChatId();
            long user_id = user.getId();

            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .text("Hello " + firstName + " " + lastName + " ("+ userName + "). Are you a human?")
                    .replyMarkup(
                        InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(
                            new InlineKeyboardRow(
                                InlineKeyboardButton
                                .builder()
                                .text("Click to confirm")
                                .callbackData("Catcha_pass")
                                .build()
                            )
                        ).build()
                    )
                    .build();
            
            ChatPermissions permission = ChatPermissions
                .builder()
                .canSendMessages(false)
                .build();

            RestrictChatMember restriction = RestrictChatMember
                .builder()
                .chatId(chat_id)
                .userId(user_id)
                .permissions(permission)
                .build();

            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            try {
                telegramClient.execute(restriction); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        
        } else if (update.hasMessage() && update.getMessage().isCommand()){

            if (update.getMessage().getText().startsWith("/ban")) {

                BanUserCommandHandler handler = new BanUserCommandHandler();

               try {
                    handler.handle(update, telegramClient);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().getText().startsWith("/unban")) {
                UnBanUserCommandHandler handler = new UnBanUserCommandHandler();

                try {
                    handler.handle(update, telegramClient);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        } else if (update.hasCallbackQuery()) {
            
            String call_data = update.getCallbackQuery().getData();

            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            
            User from = update.getCallbackQuery().getFrom();

            long user_id = from.getId();

            if (call_data.equals("Catcha_pass")) {

                String answer = "Thanks for your cooperation.";

                EditMessageText new_message = EditMessageText.builder()
                    .chatId(chat_id)
                    .messageId(toIntExact(message_id))
                    .text(answer)
                    .build();

                try {
                    telegramClient.execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                ChatPermissions permission = ChatPermissions
                    .builder()
                    .canSendMessages(true)
                    .build();

                RestrictChatMember release = RestrictChatMember
                    .builder()
                    .chatId(chat_id)
                    .userId(user_id)
                    .permissions(permission)
                    .build();
                
                try {
                    telegramClient.execute(release);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}