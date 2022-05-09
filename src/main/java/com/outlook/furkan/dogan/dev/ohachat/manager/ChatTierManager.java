package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import org.bukkit.entity.Player;

/**
 * @author Furkan Doğan
 */
public interface ChatTierManager {

  ChatTier findChatTier(Player player);

  ChatTier getChatTier(String name);

  void setChatTier(Player player, ChatTier chatTier);

  boolean setChatTier(Player player, String channel);

  void loadDefaults();
}
