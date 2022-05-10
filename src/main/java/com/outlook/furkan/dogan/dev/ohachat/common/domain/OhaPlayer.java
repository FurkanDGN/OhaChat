package com.outlook.furkan.dogan.dev.ohachat.common.domain;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public final class OhaPlayer {

  private final UUID uniqueId;
  private final Set<String> blacklist;
  private String channel;

  public OhaPlayer(UUID uniqueId,
                   Set<String> blacklist,
                   String channel) {
    this.uniqueId = uniqueId;
    this.blacklist = blacklist;
    this.channel = channel;
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public Set<String> getBlacklist() {
    return this.blacklist;
  }

  public String getChannel() {
    return this.channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    OhaPlayer ohaPlayer = (OhaPlayer) o;
    return this.uniqueId.equals(ohaPlayer.uniqueId) &&
      this.blacklist.equals(ohaPlayer.blacklist) &&
      this.channel.equals(ohaPlayer.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.uniqueId, this.blacklist, this.channel);
  }

  @Override
  public String toString() {
    return "OhaPlayer{" +
      "uniqueId=" + this.uniqueId +
      ", blacklist=" + this.blacklist +
      ", channel='" + this.channel + '\'' +
      '}';
  }
}
