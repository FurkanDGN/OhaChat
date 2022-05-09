package com.outlook.furkan.dogan.dev.ohachat.common.domain;

import java.util.Set;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public final class OhaPlayer {

  private final UUID uniqueId;
  private final Set<UUID> blacklist;
  private String channel;

  public OhaPlayer(UUID uniqueId, Set<UUID> blacklist, String channel) {
    this.uniqueId = uniqueId;
    this.blacklist = blacklist;
    this.channel = channel;
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public Set<UUID> getBlacklist() {
    return this.blacklist;
  }

  public String getChannel() {
    return this.channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
