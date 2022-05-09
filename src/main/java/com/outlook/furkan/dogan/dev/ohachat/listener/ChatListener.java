package com.outlook.furkan.dogan.dev.ohachat.listener;

import com.outlook.furkan.dogan.dev.ohachat.handler.ChatHandler;
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

  public ChatListener(ChatHandler chatHandler) {
    this.chatHandler = chatHandler;
  }

  @EventHandler
  public void onPlayerChatEvent(PlayerChatEvent event) {
    Set<Player> recipients = this.chatHandler.findRecipients(event);
    event.getRecipients().clear();
    event.getRecipients().addAll(recipients);
  }
}
