package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Furkan Doğan
 */
public class DefaultChatTierProcessor implements ChatTierProcessor {

  private final PreferencesManager preferencesManager;

  public DefaultChatTierProcessor(PreferencesManager preferencesManager) {
    this.preferencesManager = preferencesManager;
  }

  @Override
  public void process(Player sender, ChatTierType chatTierType, Map<String, Object> metadata, Set<Player> postRecipients) {
    Set<? extends Player> recipients = chatTierType.findRecipients(sender, metadata)
      .filter(recipient -> this.preferencesManager.shouldSee(sender, recipient))
      .collect(Collectors.toSet());

    postRecipients.addAll(recipients);
  }
}
