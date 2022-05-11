package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.player.OhaPlayer;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Furkan Doğan
 */
public class DefaultChatTierManager implements ChatTierManager {

  private final Map<String, ChatTier> chatTiers = new ConcurrentHashMap<>();
  private final DataSource dataSource;

  public DefaultChatTierManager(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ChatTier findChatTier(UUID uniqueId) {
    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    String channel = ohaPlayer.getChannel();

    return Optional.of(channel)
      .map(this.chatTiers::get)
      .orElse(this.chatTiers.get(DefaultChatTierName.GLOBAL));
  }

  @Override
  public ChatTier getChatTier(String name) {
    return this.chatTiers.get(name);
  }

  @Override
  public void setChatTier(Player player, ChatTier chatTier) {
    UUID uniqueId = player.getUniqueId();
    String chatTierName = chatTier.getName();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    ohaPlayer.setChannel(chatTierName);

    this.dataSource.save(ohaPlayer);
  }

  @Override
  public boolean createChatTier(String name, ChatTierType chatTierType, Map<String, Object> metadata) {
    if (this.chatTiers.containsKey(name)) {
      return false;
    } else {
      ChatTier chatTier = new ChatTier(name, chatTierType, metadata);
      this.chatTiers.put(name, chatTier);
      ConfigFile.saveChatTier(chatTier);
      return true;
    }
  }

  @Override
  public boolean deleteChatTier(String name) {
    if (!name.equals(DefaultChatTierName.GLOBAL) &&
      !name.equals(DefaultChatTierName.SHOUT) &&
      !name.equals(DefaultChatTierName.LOCAL) &&
      !name.equals(DefaultChatTierName.WHISPER)) {
      this.chatTiers.remove(name);
      ConfigFile.deleteChatTier(name);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void loadDefaults() {
    String globalName = DefaultChatTierName.GLOBAL;
    String shoutName = DefaultChatTierName.SHOUT;
    String localName = DefaultChatTierName.LOCAL;
    String whisperName = DefaultChatTierName.WHISPER;

    ChatTier global = new ChatTier(globalName, ChatTierType.GLOBAL, Collections.emptyMap());
    ChatTier shout = new ChatTier(shoutName, ChatTierType.SHOUT, Collections.emptyMap());
    ChatTier local = new ChatTier(localName, ChatTierType.LOCAL, MapUtil.map(ChatTierMetadata.RANGE, ConfigFile.localChannelRange));
    ChatTier whisper = new ChatTier(whisperName, ChatTierType.WHISPER, MapUtil.map(ChatTierMetadata.RANGE, ConfigFile.whisperChannelRange));

    this.chatTiers.put(globalName, global);
    this.chatTiers.put(shoutName, shout);
    this.chatTiers.put(localName, local);
    this.chatTiers.put(whisperName, whisper);
  }

  @Override
  public void loadCustoms() {
    ConfigFile.loadChatTiers()
      .forEach(chatTier -> {
        String name = chatTier.getName();
        this.chatTiers.put(name, chatTier);
      });
  }
}
