package com.outlook.furkan.dogan.dev.ohachat.command;

import com.gmail.furkanaxx34.dlibrary.bukkit.utils.NumberUtil;
import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.CommandPermission;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.NamePatterns;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Locale;

/**
 * @author Furkan Doğan
 */
public class OhaAdminCommand implements CommandExecutor {

  private final ChatTierManager chatTierManager;

  public OhaAdminCommand(ChatTierManager chatTierManager) {
    this.chatTierManager = chatTierManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission(CommandPermission.ADMIN_COMMAND_PERMISSION)) {
      MessageUtil.sendMessage(sender, LanguageFile.noPermission);
      return false;
    }

    if (args.length == 0) {
      MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
      return false;
    }

    String operation = args[0].toLowerCase(Locale.ENGLISH);

    switch (operation) {
      case "create":
        return this.handleCreate(sender, args);
      case "delete":
        return this.handleDelete(sender, args);
      default:
        MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
    }

    return false;
  }

  private boolean handleCreate(CommandSender sender, String[] args) {
    if (args.length < 3) {
      MessageUtil.sendMessage(sender, LanguageFile.createCommandUsage);
      return false;
    }

    String channelName = args[1];

    if (!NamePatterns.CHAT_TIER_NAME.matcher(channelName).matches()) {
      MessageUtil.sendMessage(sender, LanguageFile.invalidCharacters);
      return false;
    }

    ChatTierType channelType = ChatTierType.fromString(args[2]);

    if (channelType == null) {
      MessageUtil.sendMessage(sender, LanguageFile.invalidChannelType);
      return false;
    }

    if (channelType != ChatTierType.LOCAL && channelType != ChatTierType.WHISPER) {
      this.chatTierManager.createChatTier(channelName, channelType, Collections.emptyMap());
      MessageUtil.sendMessage(sender, LanguageFile.channelCreated, new SimpleEntry<>("%channel%", () -> channelName));
      return true;
    } else {
      if (args.length < 4) {
        MessageUtil.sendMessage(sender, LanguageFile.invalidRange);
        return false;
      }

      String rangeArg = args[3];

      boolean isInteger = NumberUtil.isFloat(rangeArg) || NumberUtil.isInteger(rangeArg);
      if (!isInteger) {
        MessageUtil.sendMessage(sender, LanguageFile.invalidRange);
        return false;
      }

      double range = Double.parseDouble(rangeArg);

      boolean isSuccess = this.chatTierManager.createChatTier(channelName, channelType, MapUtil.map(ChatTierMetadata.RANGE, range));

      if (isSuccess) {
        MessageUtil.sendMessage(sender, LanguageFile.channelCreated, new SimpleEntry<>("%channel%", () -> channelName));
        return true;
      } else {
        MessageUtil.sendMessage(sender, LanguageFile.channelAlreadyExists);
        return false;
      }
    }
  }

  private boolean handleDelete(CommandSender sender, String[] args) {
    if (args.length < 2) {
      MessageUtil.sendMessage(sender, LanguageFile.deleteCommandUsage);
      return false;
    }

    String channelName = args[1];

    boolean isDeleted = this.chatTierManager.deleteChatTier(channelName);
    if (isDeleted) {
      MessageUtil.sendMessage(sender, LanguageFile.channelDeleted, new SimpleEntry<>("%channel%", () -> channelName));
      return true;
    } else {
      MessageUtil.sendMessage(sender, LanguageFile.channelNotFound, new SimpleEntry<>("%channel%", () -> channelName));
      return false;
    }
  }
}
