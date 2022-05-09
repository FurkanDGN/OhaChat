package com.outlook.furkan.dogan.dev.ohachat.common.config;

import com.gmail.furkanaxx34.dlibrary.bukkit.transformer.resolvers.BukkitSnakeyaml;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformedObject;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformerPool;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Comment;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Exclude;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Names;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * @author Furkan Doğan
 */
@Names(modifier = Names.Modifier.TO_LOWER_CASE, strategy = Names.Strategy.HYPHEN_CASE)
public class ConfigFile extends TransformedObject {

  @Exclude
  private static TransformedObject instance;

  @Comment("Name of global channel")
  public static String globalChannelName = "global";

  @Comment("Name of shout channel")
  public static String shoutChannelName = "shout";

  @Comment("Name of local channel")
  public static String localChannelName = "local";

  public static double localChannelRange = 20.0D;

  @Comment("Name of whisper channel")
  public static String whisperChannelName = "whisper";

  public static double whisperChannelRange = 3.0D;

  private ConfigFile() {
  }

  public static void loadFile(final Plugin plugin) {
    if (ConfigFile.instance == null) {
      ConfigFile.instance = TransformerPool.create(new ConfigFile())
        .withFile(new File(plugin.getDataFolder(), "config.yml"))
        .withResolver(new BukkitSnakeyaml());
    }
    ConfigFile.instance.initiate();
  }
}
