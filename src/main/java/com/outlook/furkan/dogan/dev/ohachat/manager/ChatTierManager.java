package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import org.bukkit.entity.Player;

import java.util.Map;
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

  boolean createChatTier(String name, ChatTierType chatTierType, Map<String, Object> metadata);

  boolean deleteChatTier(String name);

  void loadDefaults();

  void loadCustoms();
}
