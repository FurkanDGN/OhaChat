package com.outlook.furkan.dogan.dev.ohachat.command;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.AbstractMap.SimpleEntry;
import java.util.Locale;

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

    String operation = label.toLowerCase(Locale.ENGLISH);

    Player player = ((Player) sender).getPlayer();
    switch (operation) {
      case "block":
        return this.handleBlock(player, args);
      case "unblock":
        return this.handleUnblock(player, args);
      default:
        return false;
    }
  }

  private boolean handleBlock(Player player, String[] args) {
    if (args.length == 0) {
      MessageUtil.sendMessage(player, LanguageFile.blockCommandUsage);
      return false;
    }

    String target = args[0];

    boolean success = this.preferencesManager.blockPlayer(player, target);
    if (success) {
      MessageUtil.sendMessage(player, LanguageFile.playerBlocked, new SimpleEntry<>("%player%", () -> target));
      return true;
    } else {
      MessageUtil.sendMessage(player, LanguageFile.alreadyPlayerBlocked);
      return false;
    }
  }

  private boolean handleUnblock(Player player, String[] args) {
    if (args.length == 0) {
      MessageUtil.sendMessage(player, LanguageFile.unblockCommandUsage);
      return false;
    }

    String target = args[0];

    boolean success = this.preferencesManager.unblockPlayer(player, target);
    if (success) {
      MessageUtil.sendMessage(player, LanguageFile.playerUnblocked, new SimpleEntry<>("%player%", () -> target));
      return true;
    } else {
      MessageUtil.sendMessage(player, LanguageFile.alreadyPlayerUnblocked);
      return false;
    }
  }
}
