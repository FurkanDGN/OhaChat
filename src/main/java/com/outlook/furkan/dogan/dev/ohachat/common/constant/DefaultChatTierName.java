package com.outlook.furkan.dogan.dev.ohachat.common.constant;

import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;

/**
 * @author Furkan Doğan
 */
public final class DefaultChatTierName {

  public static String GLOBAL = "global";
  public static String SHOUT = "shout";
  public static String LOCAL = "local";
  public static String WHISPER = "whisper";

  public static void init() {
    DefaultChatTierName.GLOBAL = ConfigFile.globalChannelName;
    DefaultChatTierName.SHOUT = ConfigFile.shoutChannelName;
    DefaultChatTierName.LOCAL = ConfigFile.localChannelName;
    DefaultChatTierName.WHISPER = ConfigFile.whisperChannelName;
  }
}
