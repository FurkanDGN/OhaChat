package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTier;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public interface ChatTierManager {

  ChatTier findChatTier(UUID uniqueId);

  default ChatTier findChatTier(Player player) {
    UUID uniqueId = player.getUniqueId();
    return this.findChatTier(uniqueId);
  }

  ChatTier getChatTier(String name);

  void setChatTier(Player player, ChatTier chatTier);

  boolean createChatTier(ChatTier chatTier);

  boolean deleteChatTier(String name);

  Collection<ChatTier> getChatTiers();

  void loadDefaults();

  void loadCustoms();
}
