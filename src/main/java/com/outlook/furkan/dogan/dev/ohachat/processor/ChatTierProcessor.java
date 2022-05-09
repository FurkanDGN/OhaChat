package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface ChatTierProcessor {

  void processGlobal(PlayerChatEvent event, Set<Player> postRecipients);

  void processShout(PlayerChatEvent event, Set<Player> postRecipients);

  void processRangedChatTier(PlayerChatEvent event, Set<Player> postRecipients, ChatTier chatTier);

}
