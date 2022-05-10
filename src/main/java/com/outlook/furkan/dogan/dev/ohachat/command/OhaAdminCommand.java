package com.outlook.furkan.dogan.dev.ohachat.command;

import com.gmail.furkanaxx34.dlibrary.bukkit.utils.NumberUtil;
import com.gmail.furkanaxx34.dlibrary.replaceable.RpString;
import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.CommandPermission;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author Furkan Doğan
 */
public class OhaAdminCommand implements CommandExecutor {

  private final static Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z\\d]+");

  private final ChatTierManager chatTierManager;

  public OhaAdminCommand(ChatTierManager chatTierManager) {
    this.chatTierManager = chatTierManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission(CommandPermission.ADMIN_COMMAND_PERMISSION)) {
      String errorMessage = LanguageFile.noPermission.build();

      sender.sendMessage(errorMessage);
      return false;
    }

    if (args.length == 0) {
      MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
      return false;
    }

    if (args[0].equalsIgnoreCase("create")) {
      if (args.length < 3) {
        MessageUtil.sendMessage(sender, LanguageFile.createCommandUsage);
        return false;
      }

      String channelName = args[1];

      if (!OhaAdminCommand.NAME_PATTERN.matcher(channelName).matches()) {
        MessageUtil.sendMessage(sender, LanguageFile.invalidCharacters);
        return false;
      }

      ChatTierType channelType = ChatTierType.fromString(args[2]);
      if (channelType == null) {
        MessageUtil.sendMessage(sender, LanguageFile.invalidChannelType);
        return false;
      }

      if (channelType == ChatTierType.LOCAL || channelType == ChatTierType.WHISPER) {
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
          String createdMessage = LanguageFile.channelCreated.build(new SimpleEntry<>("%channel%", () -> channelName));
          sender.sendMessage(createdMessage);
          return true;
        } else {
          MessageUtil.sendMessage(sender, LanguageFile.channelAlreadyExists);
          return false;
        }
      } else {
        this.chatTierManager.createChatTier(channelName, channelType, Collections.emptyMap());

        String createdMessage = LanguageFile.channelCreated.build(new SimpleEntry<>("%channel%", () -> channelName));
        sender.sendMessage(createdMessage);
      }
    } else if (args[0].equalsIgnoreCase("delete")) {
      if (args.length < 2) {
        MessageUtil.sendMessage(sender, LanguageFile.deleteCommandUsage);
        return false;
      }

      String channelName = args[1];

      boolean isDeleted = this.chatTierManager.deleteChatTier(channelName);
      if (isDeleted) {
        String createdMessage = LanguageFile.channelDeleted.build(new SimpleEntry<>("%channel%", () -> channelName));
        sender.sendMessage(createdMessage);
        return true;
      } else {
        String errorMessage = LanguageFile.channelNotFound.build(
          new SimpleEntry<>("%channel%", () -> channelName)
        );
        sender.sendMessage(errorMessage);
        return false;
      }
    } else {
      MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
    }

    return false;
  }
}
