package com.telegram_bot.handlers.commands;

// import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

class BanUserCommandOptions {
  @Parameters List<String> params;

  @Option(
      names = {"-d", "--duration"},
      description = "Ban duration.")
  int duration;

  @Option(
      names = {"--del"},
      defaultValue = "false",
      description = "Remove message or not.")
  boolean revoke_messages;

  @Option(
      names = {"--uid"},
      description = "User ID.")
  long uid;

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Provide help message.")
  boolean helpRequested;

  @Option(
      names = {"-u", "--unit"},
      description = "單位：${COMPLETION-CANDIDATES}（預設 ${DEFAULT-VALUE}）",
      defaultValue = "MINUTES")
  ChronoUnit unit;
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
      showHelpMessage(update, telegramClient, cmd, chat_id, message_id);

    } else {

      Instant now = Instant.now();

      Instant future = now.plus(opts.duration, opts.unit);

      long futureEpochSeconds = future.getEpochSecond();
      int futureSecondsInt = Math.toIntExact(futureEpochSeconds);

      Integer futureSecondsInteger = futureSecondsInt;

      System.out.println(opts.revoke_messages);

      long user_id = Long.parseLong(opts.params.get(1));

      BanChatMember banAction =
          BanChatMember.builder()
              .chatId(chat_id)
              .userId(user_id)
              .untilDate(futureSecondsInt)
              .revokeMessages(opts.revoke_messages)
              .build();

      try {
        telegramClient.execute(banAction);
      } catch (TelegramApiException e) {
        e.printStackTrace();
        return;
      }

      String message = "User " + user_id + " has been banned.";

      sendMessage(update, telegramClient, chat_id, message, message_id);
    }
  }
}
