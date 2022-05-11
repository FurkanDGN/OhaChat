package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Furkan Doğan
 */
public abstract class ChatTier {

  protected final String name;

  public ChatTier(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public abstract String getType();

  public abstract Map<String, Object> getMetadata();

  public abstract Stream<? extends Player> findRecipients(Player sender);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    ChatTier chatTier = (ChatTier) o;
    return this.name.equals(chatTier.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }
}
