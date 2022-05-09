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

  public static RpString channelCreated = RpString.from("&aChannel &e%channel% &ahas been created.")
    .regex("%channel%")
    .map(XColor::colorize);

  public static RpString channelDeleted = RpString.from("&eChannel &c%channel% &ehas been deleted.")
    .regex("%channel%")
    .map(XColor::colorize);

  public static RpString channelNotFound = RpString.from("&cChannel &e%channel% &cnot found.")
    .regex("%channel%")
    .map(XColor::colorize);

  public static RpString channelAlreadyExists = RpString.from("&cThis channel already exists.")
    .map(XColor::colorize);

  public static RpString onlyForPlayer = RpString.from("&cThis command cannot be executed by console.")
    .map(XColor::colorize);

  public static RpString createCommandUsage = RpString.from("&7Usage: &e/ohaadmin &7<&ecreate&7> <&echannel name&7> <&eglobal&7/&eshout&7/&elocal&7/&ewhisper&7> <&erange&7>")
    .map(XColor::colorize);

  public static RpString deleteCommandUsage = RpString.from("&7Usage: &e/ohaadmin &7<&edelete&7> <&echannel name&7>")
    .map(XColor::colorize);

  public static RpString pluginCommandUsage = RpString.from("&7Usage: &f/ohaadmin <&ecreate&7/&bdelete&7> <&achannel name&7> <&eglobal&7/&eshout&7/&elocal&7/&ewhisper&7> <&erange&7>")
    .map(XColor::colorize);

  public static RpString invalidCharacters = RpString.from("&cChannel name contains invalid characters.")
    .map(XColor::colorize);

  public static RpString invalidChannelType = RpString.from("&cInvalid channel type.")
    .map(XColor::colorize);

  public static RpString invalidRange = RpString.from("&cInvalid range.")
    .map(XColor::colorize);

  public static RpString noPermission = RpString.from("&cYou have not permission to execute this command.")
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
