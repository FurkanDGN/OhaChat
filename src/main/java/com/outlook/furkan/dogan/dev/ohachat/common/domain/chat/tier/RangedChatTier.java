package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Furkan Doğan
 */
public class RangedChatTier extends ChatTier {

  private final Map<String, Object> metadata;
  private final double range;

  public RangedChatTier(String name, double range) {
    super(name);
    this.range = range;
    this.metadata = MapUtil.map(ChatTierMetadata.RANGE, range);
  }

  @Override
  public String getType() {
    return "ranged";
  }

  @Override
  public Map<String, Object> getMetadata() {
    return this.metadata;
  }

  @Override
  public Stream<? extends Player> findRecipients(Player sender) {
    UUID uniqueId = sender.getUniqueId();
    Location location = sender.getLocation();

    return sender.getWorld().getPlayers()
      .stream()
      .filter(worldPlayer -> !worldPlayer.getUniqueId().equals(uniqueId))
      .filter(worldPlayer -> worldPlayer.getLocation().distance(location) <= this.range);
  }
}
