package com.outlook.furkan.dogan.dev.ohachat.processor;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
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
  public void processGlobal(PlayerChatEvent event, Set<Player> postRecipients) {
    Player sender = event.getPlayer();

    Set<? extends Player> recipients = Bukkit.getServer().getOnlinePlayers()
      .stream()
      .filter(recipient -> this.preferencesManager.shouldSee(sender, recipient))
      .collect(Collectors.toSet());

    postRecipients.addAll(recipients);
  }

  @Override
  public void processShout(PlayerChatEvent event, Set<Player> postRecipients) {
    Player sender = event.getPlayer();

    this.findSameWorldRecipients(sender, postRecipients, player -> true);
  }

  @Override
  public void processRangedChatTier(PlayerChatEvent event, Set<Player> postRecipients, ChatTier chatTier) {
    Player sender = event.getPlayer();
    Location senderLocation = sender.getLocation();
    double range = (double) chatTier.getMetadata().get(ChatTierMetadata.RANGE);

    Predicate<Player> rangePredicate = player -> player.getLocation().distance(senderLocation) <= range;

    this.findSameWorldRecipients(sender, postRecipients, rangePredicate);
  }

  private void findSameWorldRecipients(Player sender, Set<Player> postRecipients, Predicate<Player> predicate) {
    UUID senderUniqueId = sender.getUniqueId();

    Set<Player> recipients = sender.getWorld().getPlayers()
      .stream()
      .filter(worldPlayer -> !worldPlayer.getUniqueId().equals(senderUniqueId))
      .filter(recipient -> this.preferencesManager.shouldSee(sender, recipient))
      .filter(predicate)
      .collect(Collectors.toSet());

    postRecipients.addAll(recipients);
  }
}
