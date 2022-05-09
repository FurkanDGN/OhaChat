package com.outlook.furkan.dogan.dev.ohachat.common.constant;

import java.util.Arrays;

/**
 * @author Furkan Doğan
 */
public enum ChatTierType {
  GLOBAL, SHOUT, LOCAL, WHISPER;

  public static ChatTierType fromString(String name) {
    return Arrays.stream(ChatTierType.values())
      .filter(chatTierType -> chatTierType.name().equals(name))
      .findAny()
      .orElse(null);
  }
}
