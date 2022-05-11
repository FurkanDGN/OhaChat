package com.outlook.furkan.dogan.dev.ohachat.hook;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public class PAPIHook extends PlaceholderExpansion {

  private final ChatTierManager chatTierManager;

  public PAPIHook(ChatTierManager chatTierManager) {
    this.chatTierManager = chatTierManager;
  }

  @Override
  public String getIdentifier() {
    return "ohachat";
  }

  @Override
  public String getAuthor() {
    return "Dantero";
  }

  @Override
  public String getVersion() {
    return "1.0";
  }

  @Override
  public String onRequest(OfflinePlayer player, String params) {
    if (params.equalsIgnoreCase("channel")) {
      UUID uniqueId = player.getUniqueId();
      ChatTier chatTier = this.chatTierManager.findChatTier(uniqueId);
      return chatTier.getName();
    }

    return null;
  }
}
