package com.outlook.furkan.dogan.dev.ohachat.handler;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.exception.UnsupportedTierException;
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
    ChatTierType tierType = chatTier.getChatTierType();
    switch (tierType) {
      case GLOBAL:
        this.chatTierProcessor.processGlobal(event, postRecipients);
        break;
      case SHOUT:
        this.chatTierProcessor.processShout(event, postRecipients);
        break;
      case LOCAL:
      case WHISPER:
        this.chatTierProcessor.processRangedChatTier(event, postRecipients, chatTier);
        break;
      default:
        throw new UnsupportedTierException("Unsupported tier [" + tierType + "] type!");
    }
  }
}
