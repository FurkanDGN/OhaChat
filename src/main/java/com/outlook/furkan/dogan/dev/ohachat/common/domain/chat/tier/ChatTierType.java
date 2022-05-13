package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Furkan Doğan
 */
public enum ChatTierType {

  GLOBAL("global") {
    @Override
    public Stream<? extends Player> findRecipients(Player sender, Map<String, Object> metadata) {
      UUID uniqueId = sender.getUniqueId();

      return Bukkit.getServer().getOnlinePlayers()
        .stream()
        .filter(onlinePlayer -> !onlinePlayer.getUniqueId().equals(uniqueId));
    }
  },
  WORLD("world") {
    @Override
    public Stream<? extends Player> findRecipients(Player sender, Map<String, Object> metadata) {
      UUID uniqueId = sender.getUniqueId();

      return sender.getWorld().getPlayers()
        .stream()
        .filter(worldPlayer -> !worldPlayer.getUniqueId().equals(uniqueId));
    }
  },
  RANGED("ranged") {
    @Override
    public Stream<? extends Player> findRecipients(Player sender, Map<String, Object> metadata) {
      double range = (double) metadata.get(Metadata.RANGE);

      UUID uniqueId = sender.getUniqueId();
      Location location = sender.getLocation();

      return sender.getWorld().getPlayers()
        .stream()
        .filter(worldPlayer -> !worldPlayer.getUniqueId().equals(uniqueId))
        .filter(worldPlayer -> worldPlayer.getLocation().distance(location) <= range);
    }
  };

  public abstract Stream<? extends Player> findRecipients(Player sender, Map<String, Object> metadata);

  private final String name;

  ChatTierType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
