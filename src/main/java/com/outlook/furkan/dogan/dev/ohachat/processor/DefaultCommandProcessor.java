package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import org.bukkit.entity.Player;

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
        player.sendMessage("kanalın ayarlandı.");
      }

      return true;
    } else {
      return false;
    }
  }
}
