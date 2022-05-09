package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Furkan Doğan
 */
public interface ChatTierManager {

  ChatTier findChatTier(Player player);

  ChatTier getChatTier(String name);

  void setChatTier(Player player, ChatTier chatTier);

  void createChatTier(String name, ChatTierType chatTierType, Map<String, Object> metadata);

  boolean deleteChatTier(String name);

  void loadDefaults();
}
