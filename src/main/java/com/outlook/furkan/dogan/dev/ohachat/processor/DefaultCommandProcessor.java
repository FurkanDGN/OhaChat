package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import org.bukkit.entity.Player;

import java.util.AbstractMap;

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
    ChatTier chatTier = this.chatTierManager.getChatTier(channel);

    if (chatTier != null) {
      ChatTier oldChatTier = this.chatTierManager.findChatTier(player);

      this.chatTierManager.setChatTier(player, chatTier);

      if (message != null) {
        player.chat(message);
        this.chatTierManager.setChatTier(player, oldChatTier);
      } else {
        String successMessage = LanguageFile.channelSet.build(
          new AbstractMap.SimpleEntry<>("%channel%", () -> channel)
        );

        player.sendMessage(successMessage);
      }

      return true;
    } else {
      return false;
    }
  }
}
