package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface ChatTierProcessor {

  void process(Player sender, ChatTierType chatTierType, Map<String, Object> metadata, Set<Player> postRecipients);
}
