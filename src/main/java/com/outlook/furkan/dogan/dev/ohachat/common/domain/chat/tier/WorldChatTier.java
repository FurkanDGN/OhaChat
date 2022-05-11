package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Furkan Doğan
 */
public class WorldChatTier extends ChatTier {

  public WorldChatTier(String name) {
    super(name);
  }

  @Override
  public String getType() {
    return "world";
  }

  @Override
  public Stream<Player> findRecipients(Player sender) {
    UUID uniqueId = sender.getUniqueId();

    return sender.getWorld().getPlayers()
      .stream()
      .filter(worldPlayer -> !worldPlayer.getUniqueId().equals(uniqueId));
  }

  @Override
  public Map<String, Object> getMetadata() {
    return Collections.emptyMap();
  }

  @Override
  public String toString() {
    return "WorldChatTier{" +
      "name='" + this.name + '\'' +
      '}';
  }
}
