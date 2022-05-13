package com.outlook.furkan.dogan.dev.ohachat.handler;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.Metadata;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.processor.ChatTierProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Furkan Doğan
 */
@SuppressWarnings("deprecation")
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

    String channel = this.chatTierManager.getChannelName(player);
    Map<String, Object> metadata = this.chatTierManager.getChannelMetadata(channel);
    ChatTierType chatTierType = (ChatTierType) metadata.get(Metadata.TYPE);


    this.chatTierProcessor.process(player, chatTierType, metadata, postRecipients);
  }
}
