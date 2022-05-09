package com.outlook.furkan.dogan.dev.ohachat.listener;

import com.outlook.furkan.dogan.dev.ohachat.handler.ChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.CommandHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public class ChatListener implements Listener {

  private final ChatHandler chatHandler;
  private final CommandHandler commandHandler;

  public ChatListener(ChatHandler chatHandler,
                      CommandHandler commandHandler) {
    this.chatHandler = chatHandler;
    this.commandHandler = commandHandler;
  }

  @EventHandler
  public void onAsyncPlayerChatEvent(PlayerChatEvent event) {
    if (event.getMessage().startsWith("/")) {
      Player player = event.getPlayer();
      String message = event.getMessage();

      boolean shouldCancel = this.commandHandler.handle(player, message);
      event.setCancelled(shouldCancel);
    } else {
      Set<Player> recipients = this.chatHandler.findRecipients(event);
      event.getRecipients().clear();
      event.getRecipients().addAll(recipients);
    }
  }
}
