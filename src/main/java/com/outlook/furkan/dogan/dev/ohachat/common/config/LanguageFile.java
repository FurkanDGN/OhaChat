package com.outlook.furkan.dogan.dev.ohachat.common.config;

import com.gmail.furkanaxx34.dlibrary.bukkit.color.XColor;
import com.gmail.furkanaxx34.dlibrary.bukkit.transformer.resolvers.BukkitSnakeyaml;
import com.gmail.furkanaxx34.dlibrary.replaceable.RpString;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformedObject;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformerPool;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Exclude;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Names;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * @author Furkan Doğan
 */
@Names(modifier = Names.Modifier.TO_LOWER_CASE, strategy = Names.Strategy.HYPHEN_CASE)
public class LanguageFile extends TransformedObject {

  @Exclude
  private static TransformedObject instance;

  public static RpString channelSet = RpString.from("&aYour channel set to &e%channel%&a.")
    .regex("%channel%")
    .map(XColor::colorize);

  public static RpString channelNotFound = RpString.from("&cChannel &e%channel% &cnot found.")
    .regex("%channel%")
    .map(XColor::colorize);

  public static RpString onlyForPlayer = RpString.from("&cThis command cannot be executed by console.")
    .map(XColor::colorize);

  private LanguageFile() {
  }

  public static void loadFile(final Plugin plugin) {
    if (LanguageFile.instance == null) {
      LanguageFile.instance = TransformerPool.create(new LanguageFile())
        .withFile(new File(plugin.getDataFolder(), "language.yml"))
        .withResolver(new BukkitSnakeyaml());
    }
    LanguageFile.instance.initiate();
  }
}
