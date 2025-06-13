package com.telegram_bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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

class StartCommandOptions {
    
    @Option(names = {"-v", "--verbose"}, description = "Print verbose output")
    boolean verbose;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Provide help message.")
    boolean helpRequested;
}


public class StartCommandHandler implements GenericHandler {

    @Override
    public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {

        long chat_id = update.getMessage().getChatId();

        String[] argv = update.getMessage().getText().split("\\s+");

        StartCommandOptions opts = new StartCommandOptions();

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
            
            SendMessage message = SendMessage
                .builder()
                .chatId(chat_id)
                .text(usageMessage)
                .build();
            telegramClient.execute(message);
            
        } else {
            System.out.println(opts.verbose);

            SendMessage message = SendMessage
                .builder()
                .chatId(chat_id)
                .text("Hello")
                .build();
            
            telegramClient.execute(message);

        }


    }
}