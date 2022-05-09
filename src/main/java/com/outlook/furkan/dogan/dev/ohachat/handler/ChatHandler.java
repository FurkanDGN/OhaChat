package com.outlook.furkan.dogan.dev.ohachat.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface ChatHandler {

  Set<Player> findRecipients(PlayerChatEvent event);
}
