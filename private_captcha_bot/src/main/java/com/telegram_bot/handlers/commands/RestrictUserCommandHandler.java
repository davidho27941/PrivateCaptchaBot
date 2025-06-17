package com.telegram_bot.handlers.commands;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

class RestrictUserCommandOptions {
  @Parameters List<String> params;

  @Option(
      names = {"--message"},
      defaultValue = "false",
      description = "Can send messages.")
  boolean can_send_messages;

  @Option(
      names = {"--audio"},
      defaultValue = "false",
      description = "Can send audios.")
  boolean can_send_audios;

  @Option(
      names = {"--documents"},
      defaultValue = "false",
      description = "Can send documents.")
  boolean can_send_documents;

  @Option(
      names = {"--photo"},
      defaultValue = "false",
      description = "Can send photos.")
  boolean can_send_photos;

  @Option(
      names = {"--video"},
      defaultValue = "false",
      description = "Can send video.")
  boolean can_send_videos;

  @Option(
      names = {"--video-note"},
      defaultValue = "false",
      description = "Can send video note.")
  boolean can_send_video_notes;

  @Option(
      names = {"--voice-note"},
      defaultValue = "false",
      description = "Can send voice note.")
  boolean can_send_voice_notes;

  @Option(
      names = {"--poll"},
      defaultValue = "false",
      description = "Can send polls.")
  boolean can_send_polls;

  @Option(
      names = {"--other-messages"},
      defaultValue = "false",
      description = "Can send other messages.")
  boolean can_send_other_messages;

  @Option(
      names = {"--web-preview"},
      defaultValue = "false",
      description = "Can send web previews.")
  boolean can_add_web_page_previews;

  @Option(
      names = {"--change-info"},
      defaultValue = "false",
      description = "Can change info.")
  boolean can_change_info;

  @Option(
      names = {"--invite"},
      defaultValue = "false",
      description = "Can sinvite users.")
  boolean can_invite_users;

  @Option(
      names = {"--pin-message"},
      defaultValue = "false",
      description = "Can pin message.")
  boolean can_pin_messages;

  @Option(
      names = {"--manage-topic"},
      defaultValue = "false",
      description = "Can manage topic.")
  boolean can_manage_topics;

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Provide help message.")
  boolean helpRequested;

  @Option(names = "--allow-all", description = "Enable all permissions")
  boolean allow_all;
}

public class RestrictUserCommandHandler extends CommandHandler {

  @Override
  public void handle(Update update, TelegramClient telegramClient) throws TelegramApiException {

    long chat_id = update.getMessage().getChatId();
    int message_id = update.getMessage().getMessageId();

    String[] argv = update.getMessage().getText().split("\\s+");

    RestrictUserCommandOptions opts = new RestrictUserCommandOptions();

    CommandLine cmd = new CommandLine(opts);

    try {

      CommandLine.ParseResult result = cmd.parseArgs(argv);

    } catch (ParameterException e) {
      e.getMessage();
    }

    if (opts.helpRequested) {
      showHelpMessage(update, telegramClient, cmd, chat_id, message_id);

    } else {

      System.out.println(opts.can_send_messages);

      long user_id = Long.parseLong(opts.params.get(1));

      if (opts.allow_all) {
        opts.can_send_messages = true;
        opts.can_send_audios = true;
        opts.can_send_documents = true;
        opts.can_send_photos = true;
        opts.can_send_videos = true;
        opts.can_send_video_notes = true;
        opts.can_send_voice_notes = true;
        opts.can_send_polls = true;
        opts.can_send_other_messages = true;
        opts.can_add_web_page_previews = true;
        opts.can_change_info = true;
        opts.can_invite_users = true;
        opts.can_pin_messages = true;
        opts.can_manage_topics = true;
      }

      ChatPermissions permission =
          ChatPermissions.builder()
              .canSendMessages(opts.can_send_messages)
              .canSendAudios(opts.can_send_audios)
              .canSendDocuments(opts.can_send_documents)
              .canSendPhotos(opts.can_send_photos)
              .canSendVideos(opts.can_send_videos)
              .canSendVideoNotes(opts.can_send_video_notes)
              .canSendVoiceNotes(opts.can_send_voice_notes)
              .canSendPolls(opts.can_send_polls)
              .canSendOtherMessages(opts.can_send_other_messages)
              .canAddWebPagePreviews(opts.can_add_web_page_previews)
              .canChangeInfo(opts.can_change_info)
              .canInviteUsers(opts.can_invite_users)
              .canPinMessages(opts.can_pin_messages)
              .canManageTopics(opts.can_manage_topics)
              .build();

      RestrictChatMember restriction =
          RestrictChatMember.builder()
              .chatId(chat_id)
              .userId(user_id)
              .permissions(permission)
              .build();

      try {
        telegramClient.execute(restriction);
      } catch (TelegramApiException e) {
        e.printStackTrace();
        return;
      }

      String message = "The permission of User " + user_id + " has beed changed.";

      sendMessage(update, telegramClient, chat_id, message, message_id);
    }
  }
}
