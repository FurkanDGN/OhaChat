package com.outlook.furkan.dogan.dev.ohachat.command;

import com.gmail.furkanaxx34.dlibrary.bukkit.utils.NumberUtil;
import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.CommandPermission;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.NamePatterns;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.GlobalChatTier;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.RangedChatTier;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.WorldChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierCommandManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Furkan Doğan
 */
public class OhaAdminCommand implements CommandExecutor {

  private static final List<String> CHAT_TIER_TYPES = Arrays.asList(
    "global",
    "world",
    "ranged"
  );

  private final ChatTierManager chatTierManager;
  private final ChatTierCommandManager chatTierCommandManager;

  public OhaAdminCommand(ChatTierManager chatTierManager,
                         ChatTierCommandManager chatTierCommandManager) {
    this.chatTierManager = chatTierManager;
    this.chatTierCommandManager = chatTierCommandManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission(CommandPermission.ADMIN_COMMAND_PERMISSION)) {
      MessageUtil.sendMessage(sender, LanguageFile.noPermission);
      return false;
    } else if (args.length == 0) {
      MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
      return false;
    } else {
      return this.handleCommand(sender, args);
    }
  }

  private boolean handleCommand(CommandSender sender, String[] args) {
    String operation = args[0].toLowerCase(Locale.ENGLISH);
    switch (operation) {
      case "create":
        return this.handleCreate(sender, args);
      case "delete":
        return this.handleDelete(sender, args);
      case "list":
        return this.handleList(sender);
      default:
        MessageUtil.sendMessage(sender, LanguageFile.pluginCommandUsage);
        return false;
    }
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

    String channelType = args[2].toLowerCase(Locale.ENGLISH);

    if (!OhaAdminCommand.CHAT_TIER_TYPES.contains(channelType)) {
      MessageUtil.sendMessage(sender, LanguageFile.invalidChannelType);
      return false;
    }

    ChatTier chatTier = null;

    switch (channelType) {
      case "global":
        chatTier = new GlobalChatTier(channelName);
        break;
      case "world":
        chatTier = new WorldChatTier(channelName);
        break;
      case "ranged":
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

        chatTier = new RangedChatTier(channelName, range);
        break;
    }

    boolean success = this.chatTierManager.createChatTier(chatTier);
    if (success) {
      this.chatTierCommandManager.registerCommand(channelName);
      MessageUtil.sendMessage(sender, LanguageFile.channelCreated, new SimpleEntry<>("%channel%", () -> channelName));
      return true;
    } else {
      MessageUtil.sendMessage(sender, LanguageFile.channelAlreadyExists);
      return false;
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
      this.chatTierCommandManager.unregisterCommand(channelName);
      MessageUtil.sendMessage(sender, LanguageFile.channelDeleted, new SimpleEntry<>("%channel%", () -> channelName));
      return true;
    } else {
      MessageUtil.sendMessage(sender, LanguageFile.channelNotFound, new SimpleEntry<>("%channel%", () -> channelName));
      return false;
    }
  }

  private boolean handleList(CommandSender sender) {
    Map<String, List<String>> namesByChatTierType = this.chatTierManager.getChatTiers()
      .stream()
      .map(chatTier -> new SimpleEntry<>(chatTier.getType(), chatTier.getName()))
      .collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

    namesByChatTierType
      .forEach((chatTierType, nameList) -> {
        String names = String.join(", ", nameList);

        MessageUtil.sendMessage(sender, LanguageFile.channelsInfo,
          new SimpleEntry<>("%channel_type%", () -> chatTierType),
          new SimpleEntry<>("%count%", () -> String.valueOf(nameList.size())),
          new SimpleEntry<>("%channels%", () -> names)
        );
      });

    return true;
  }
}
