package com.outlook.furkan.dogan.dev.ohachat.command;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractMap.SimpleEntry;

/**
 * @author Furkan Doğan
 */
public class BlockCommand implements CommandExecutor {

  private final PreferencesManager preferencesManager;

  public BlockCommand(PreferencesManager preferencesManager) {
    this.preferencesManager = preferencesManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      MessageUtil.sendMessage(sender, LanguageFile.onlyForPlayer);
      return false;
    }

    if (label.equalsIgnoreCase("block")) {
      if (args.length == 0) {
        MessageUtil.sendMessage(sender, LanguageFile.blockCommandUsage);
        return false;
      }

      String target = args[0];
      Player player = ((Player) sender).getPlayer();

      boolean success = this.preferencesManager.blockPlayer(player, target);
      if (success) {
        MessageUtil.sendMessage(sender, LanguageFile.playerBlocked, new SimpleEntry<>("%player%", () -> target));
      } else {
        MessageUtil.sendMessage(sender, LanguageFile.alreadyPlayerBlocked);
      }
    } else if (label.equalsIgnoreCase("unblock")) {
      if (args.length == 0) {
        MessageUtil.sendMessage(sender, LanguageFile.unblockCommandUsage);
        return false;
      }

      String target = args[0];
      Player player = ((Player) sender).getPlayer();

      boolean success = this.preferencesManager.unblockPlayer(player, target);
      if (success) {
        MessageUtil.sendMessage(sender, LanguageFile.playerUnblocked, new SimpleEntry<>("%player%", () -> target));
      } else {
        MessageUtil.sendMessage(sender, LanguageFile.alreadyPlayerUnblocked);
      }
    }
    return false;
  }
}
