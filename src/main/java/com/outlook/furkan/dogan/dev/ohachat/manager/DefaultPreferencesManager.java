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
    String name = sender.getName();
    UUID recipientUniqueId = recipient.getUniqueId();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(recipientUniqueId);
    Set<String> blacklist = ohaPlayer.getBlacklist();

    return !blacklist.contains(name);
  }

  @Override
  public boolean blockPlayer(Player player, String target) {
    UUID recipientUniqueId = player.getUniqueId();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(recipientUniqueId);
    Set<String> blacklist = ohaPlayer.getBlacklist();
    if (!blacklist.contains(target)) {
      return false;
    }

    blacklist.add(target);

    this.dataSource.save(ohaPlayer);
    return true;
  }

  @Override
  public boolean unblockPlayer(Player player, String target) {
    UUID recipientUniqueId = player.getUniqueId();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(recipientUniqueId);
    Set<String> blacklist = ohaPlayer.getBlacklist();
    if (!blacklist.contains(target)) {
      return false;
    }
    blacklist.remove(target);

    this.dataSource.save(ohaPlayer);
    return true;
  }
}
