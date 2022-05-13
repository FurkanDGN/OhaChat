package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.AbstractMap.SimpleEntry;

/**
 * @author Furkan Doğan
 */
public class DefaultCommandProcessor implements CommandProcessor {

  private final ChatTierManager chatTierManager;

  public DefaultCommandProcessor(ChatTierManager chatTierManager) {
    this.chatTierManager = chatTierManager;
  }

  @Override
  public boolean process(Player player, String channel, String message) {
    ChatTierType chatTier = this.chatTierManager.getChatTierType(channel);

    if (chatTier == null) {
      return false;
    } else {
      this.chatTierManager.setChatTier(player, chatTier);
      this.processMessage(player, channel, message);
      return true;
    }
  }

  private void processMessage(Player player, String channel, String message) {
    ChatTierType oldChatTier = this.chatTierManager.findChatTierType(player);

    if (message == null || message.isEmpty()) {
      MessageUtil.sendMessage(player, LanguageFile.channelSet, new SimpleEntry<>("%channel%", () -> channel));
    } else {
      player.chat(message);
      this.chatTierManager.setChatTier(player, oldChatTier);
    }
  }
}
