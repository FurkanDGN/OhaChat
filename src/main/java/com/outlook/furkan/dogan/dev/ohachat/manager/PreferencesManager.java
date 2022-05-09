package com.outlook.furkan.dogan.dev.ohachat.manager;

import org.bukkit.entity.Player;

/**
 * @author Furkan Doğan
 */
public interface PreferencesManager {

  boolean shouldSee(Player sender, Player recipient);
}
