package com.outlook.furkan.dogan.dev.ohachat.processor;

import org.bukkit.entity.Player;

/**
 * @author Furkan Doğan
 */
public interface CommandProcessor {

  boolean process(Player player, String channel, String message);
}
