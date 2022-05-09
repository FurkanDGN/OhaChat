package com.outlook.furkan.dogan.dev.ohachat.handler;

import org.bukkit.entity.Player;

/**
 * @author Furkan Doğan
 */
public interface CommandHandler {

  boolean handle(Player player, String input);
}
