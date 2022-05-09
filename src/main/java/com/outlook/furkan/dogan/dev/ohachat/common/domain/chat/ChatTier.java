package com.outlook.furkan.dogan.dev.ohachat.common.domain.chat;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;

import java.util.Map;
import java.util.Objects;

/**
 * @author Furkan Doğan
 */
public class ChatTier {

  private final String name;
  private final ChatTierType chatTierType;
  private final Map<String, Object> metadata;

  public ChatTier(String name,
                  ChatTierType chatTierType,
                  Map<String, Object> metadata) {
    this.name = name;
    this.chatTierType = chatTierType;
    this.metadata = metadata;
  }

  public String getName() {
    return this.name;
  }

  public ChatTierType getChatTierType() {
    return this.chatTierType;
  }

  public Map<String, Object> getMetadata() {
    return this.metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    ChatTier chatTier = (ChatTier) o;
    return this.name.equals(chatTier.name) &&
      this.chatTierType == chatTier.chatTierType &&
      this.metadata.equals(chatTier.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.chatTierType, this.metadata);
  }

  @Override
  public String toString() {
    return "ChatTier{" +
      "name='" + this.name + '\'' +
      ", chatTierType=" + this.chatTierType +
      ", metadata=" + this.metadata +
      '}';
  }
}
