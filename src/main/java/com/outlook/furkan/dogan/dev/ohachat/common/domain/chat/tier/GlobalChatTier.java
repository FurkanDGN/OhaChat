package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Furkan Doğan
 */
public class GlobalChatTier extends ChatTier {

  public GlobalChatTier(String name) {
    super(name);
  }

  @Override
  public String getType() {
    return "global";
  }

  @Override
  public Stream<? extends Player> findRecipients(Player sender) {
    UUID uniqueId = sender.getUniqueId();

    return Bukkit.getServer().getOnlinePlayers()
      .stream()
      .filter(onlinePlayer -> !onlinePlayer.getUniqueId().equals(uniqueId));
  }

  @Override
  public Map<String, Object> getMetadata() {
    return Collections.emptyMap();
  }

  @Override
  public String toString() {
    return "GlobalChatTier{" +
      "name='" + this.name + '\'' +
      '}';
  }
}
