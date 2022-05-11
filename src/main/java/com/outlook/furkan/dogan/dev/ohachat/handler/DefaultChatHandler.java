package com.outlook.furkan.dogan.dev.ohachat.handler;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.processor.ChatTierProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Furkan Doğan
 */
public class DefaultChatHandler implements ChatHandler {

  private final ChatTierManager chatTierManager;
  private final ChatTierProcessor chatTierProcessor;

  public DefaultChatHandler(ChatTierManager chatTierManager,
                            ChatTierProcessor chatTierProcessor) {
    this.chatTierManager = chatTierManager;
    this.chatTierProcessor = chatTierProcessor;
  }

  public Set<Player> findRecipients(PlayerChatEvent event) {
    Set<Player> postRecipients = new HashSet<>();
    Player player = event.getPlayer();

    this.handle(event, postRecipients);
    postRecipients.add(player);

    return postRecipients;
  }

  private void handle(PlayerChatEvent event, Set<Player> postRecipients) {
    Player player = event.getPlayer();

    ChatTier chatTier = this.chatTierManager.findChatTier(player);
    this.chatTierProcessor.process(player, chatTier, postRecipients);
  }
}
