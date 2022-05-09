package com.outlook.furkan.dogan.dev.ohachat.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;

/**
 * @author Furkan Doğan
 */
public class NmsCommandUtil {

  public static void registerCommand(String command, BukkitCommand commandClass) {
    try {
      final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

      bukkitCommandMap.setAccessible(true);
      CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

      commandMap.register(command, commandClass);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
