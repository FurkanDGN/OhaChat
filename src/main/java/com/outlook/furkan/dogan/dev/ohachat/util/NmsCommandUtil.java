package com.outlook.furkan.dogan.dev.ohachat.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Objects;

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

      bukkitCommandMap.setAccessible(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void unregisterCommand(String command) {
    try {
      final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

      bukkitCommandMap.setAccessible(true);
      CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

      Objects.requireNonNull(commandMap.getCommand(command)).unregister(commandMap);

      bukkitCommandMap.setAccessible(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
