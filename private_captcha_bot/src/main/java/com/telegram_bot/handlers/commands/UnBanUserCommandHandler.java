package com.telegram_bot.handlers.commands;

// import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
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

class UnBanUserCommandOptions {
    @Parameters
    List<String> params;

    @Option(names = {"-d", "--duration"}, description = "Ban duration.")
    int duration;

    @Option(names = {"--uid"}, description = "User ID.")
    long uid;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Provide help message.")
    boolean helpRequested;

}


public class UnBanUserCommandHandler extends CommandHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {

        long chat_id = update.getMessage().getChatId();
        int message_id = update.getMessage().getMessageId();

        String[] argv = update.getMessage().getText().split("\\s+");

        UnBanUserCommandOptions opts = new UnBanUserCommandOptions();

        CommandLine cmd = new CommandLine(opts);

        try {

            CommandLine.ParseResult result = cmd.parseArgs(argv);

        } catch (ParameterException e) {
            e.getMessage();
        }

        if (opts.helpRequested) {
            showHelpMessage(update, telegramClient, cmd, chat_id, message_id);

        } else {

            long user_id = Long.parseLong(opts.params.get(1));

            UnbanChatMember banAction = UnbanChatMember
                .builder()
                .chatId(chat_id)
                .userId(user_id)
                .build();

            try {
                telegramClient.execute(banAction);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return;
            }
            
            String message = "User " + user_id + " has beed unbanned.";

            sendMessage(update, telegramClient, chat_id, message, message_id);

        }

    }

}