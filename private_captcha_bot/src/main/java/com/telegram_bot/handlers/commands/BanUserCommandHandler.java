package com.telegram_bot.handlers.commands;

// import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.telegram_bot.interfaces.GenericHandler;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParameterException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import picocli.CommandLine.Help.Ansi;

class BanUserCommandOptions {
    @Parameters
    List<String> params;

    @Option(names = {"-d", "--duration"}, description = "Ban duration.")
    int duration;

    @Option(names = {"--uid"}, description = "User ID.")
    long uid;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Provide help message.")
    boolean helpRequested;

}


public class BanUserCommandHandler extends CommandHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {

        long chat_id = update.getMessage().getChatId();
        int message_id = update.getMessage().getMessageId();

        String[] argv = update.getMessage().getText().split("\\s+");

        BanUserCommandOptions opts = new BanUserCommandOptions();

        CommandLine cmd = new CommandLine(opts);

        try {

            CommandLine.ParseResult result = cmd.parseArgs(argv);

        } catch (ParameterException e) {
            e.getMessage();
        }

        if (opts.helpRequested) {
            StringWriter sw = new StringWriter();
            cmd.usage(new PrintWriter(sw), Ansi.OFF);
            String usageMessage = sw.toString();

            sendMessage(update, telegramClient, chat_id, usageMessage, message_id);


        } else {

            long user_id = Long.parseLong(opts.params.get(1));

            BanChatMember banAction = BanChatMember
                .builder()
                .chatId(chat_id)
                .userId(user_id)
                .build();

            // ChatPermissions permission = ChatPermissions
            //     .builder()
            //     .canSendMessages(false)
            //     .build();

            // RestrictChatMember restriction = RestrictChatMember
            //     .builder()
            //     .chatId(chat_id)
            //     .userId(user_id)
            //     .permissions(permission)
            //     .build();
            
            try {
                telegramClient.execute(banAction);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return;
            }
            
            String message = "User " + user_id + " has beed banned.";

            sendMessage(update, telegramClient, chat_id, message, message_id);

        }

    }

}