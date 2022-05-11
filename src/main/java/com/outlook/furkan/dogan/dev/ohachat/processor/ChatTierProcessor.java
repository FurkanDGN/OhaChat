package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTier;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface ChatTierProcessor {

  void process(Player sender, ChatTier chatTier, Set<Player> postRecipients);
}
