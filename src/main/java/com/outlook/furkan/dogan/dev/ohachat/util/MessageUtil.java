package com.outlook.furkan.dogan.dev.ohachat.util;

import com.gmail.furkanaxx34.dlibrary.replaceable.RpString;
import org.bukkit.command.CommandSender;

import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * @author Furkan Doğan
 */
public class MessageUtil {

  public static void sendMessage(CommandSender sender, RpString rpString) {
    String message = rpString.build();
    sender.sendMessage(message);
  }

  public static void sendMessage(CommandSender sender, RpString rpString, Entry<String, Supplier<String>> placeholder) {
    String message = rpString.build(placeholder);
    sender.sendMessage(message);
  }

  @SafeVarargs
  public static void sendMessage(CommandSender sender, RpString rpString, Entry<String, Supplier<String>>... placeholders) {
    String message = rpString.build(placeholders);
    sender.sendMessage(message);
  }
}
