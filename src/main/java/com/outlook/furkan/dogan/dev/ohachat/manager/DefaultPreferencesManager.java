package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.OhaPlayer;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public class DefaultPreferencesManager implements PreferencesManager {

  private final DataSource dataSource;

  public DefaultPreferencesManager(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public boolean shouldSee(Player sender, Player recipient) {
    UUID senderUniqueId = sender.getUniqueId();
    UUID recipientUniqueId = recipient.getUniqueId();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(recipientUniqueId);
    Set<UUID> blacklist = ohaPlayer.getBlacklist();

    return !blacklist.contains(senderUniqueId);
  }
}
