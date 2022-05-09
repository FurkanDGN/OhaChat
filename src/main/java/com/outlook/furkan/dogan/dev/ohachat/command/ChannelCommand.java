package com.outlook.furkan.dogan.dev.ohachat.command;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.handler.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * @author Furkan Doğan
 */
public final class ChannelCommand extends BukkitCommand {

  private final CommandHandler commandHandler;

  public ChannelCommand(String name, CommandHandler commandHandler) {
    super(name);
    this.description = "Changes the channel tier.";
    this.setAliases(new ArrayList<>());

    this.commandHandler = commandHandler;
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      String message = String.format("/%s %s", commandLabel, this.buildMessage(args));

      return this.commandHandler.handle(player, message);
    } else {
      String errorMessage = LanguageFile.onlyForPlayer.build();

      sender.sendMessage(errorMessage);
      return false;
    }
  }

  private String buildMessage(String[] args) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      stringBuilder.append(args[i]);
      if (i != args.length - 1) {
        stringBuilder.append(" ");
      }
    }
    return stringBuilder.toString();
  }
}
