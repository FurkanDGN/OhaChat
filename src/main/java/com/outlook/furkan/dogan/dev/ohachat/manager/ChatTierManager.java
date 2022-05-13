package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public interface ChatTierManager {

  ChatTierType findChatTierType(UUID uniqueId);

  default ChatTierType findChatTierType(Player player) {
    UUID uniqueId = player.getUniqueId();
    return this.findChatTierType(uniqueId);
  }

  ChatTierType getChatTierType(String name);

  Map<String, Object> getChannelMetadata(String channel);

  String getChannelName(Player player);

  void setChatTier(Player player, ChatTierType chatTierType);

  boolean createChannel(String channelName, Map<String, Object> metadata);

  boolean deleteChannel(String name);

  Collection<String> getChannels();

  void loadDefaults();

  void loadCustoms();
}
