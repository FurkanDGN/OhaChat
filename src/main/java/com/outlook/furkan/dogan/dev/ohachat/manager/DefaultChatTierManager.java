package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.Metadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.player.OhaPlayer;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Furkan Doğan
 */
public class DefaultChatTierManager implements ChatTierManager {

  private final Map<String, Map<String, Object>> channels = new ConcurrentHashMap<>();
  private final DataSource dataSource;

  public DefaultChatTierManager(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ChatTierType findChatTierType(UUID uniqueId) {
    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    String channel = ohaPlayer.getChannel();

    return Optional.of(channel)
      .map(name -> {
        Map<String, Object> metadata = this.channels.get(name);
        return (ChatTierType) metadata.get(Metadata.TYPE);
      })
      .orElse(ChatTierType.GLOBAL);
  }

  @Override
  public ChatTierType getChatTierType(String channel) {
    Map<String, Object> metadata = this.channels.get(channel);
    return (ChatTierType) metadata.get(Metadata.TYPE);
  }

  @Override
  public Map<String, Object> getChannelMetadata(String channel) {
    return this.channels.get(channel);
  }

  @Override
  public String getChannelName(Player player) {
    UUID uniqueId = player.getUniqueId();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    return ohaPlayer.getChannel();
  }

  @Override
  public void setChatTier(Player player, ChatTierType chatTierType) {
    UUID uniqueId = player.getUniqueId();
    String chatTierName = chatTierType.getName();

    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    ohaPlayer.setChannel(chatTierName);

    this.dataSource.save(ohaPlayer);
  }

  @Override
  public boolean createChannel(String channelName, Map<String, Object> metadata) {
    if (this.channels.containsKey(channelName)) {
      return false;
    } else {
      this.channels.put(channelName, Collections.unmodifiableMap(metadata));
      ConfigFile.saveChatTier(channelName, metadata);
      return true;
    }
  }

  @Override
  public boolean deleteChannel(String name) {
    if (!name.equals(DefaultChatTierName.GLOBAL) &&
      !name.equals(DefaultChatTierName.SHOUT) &&
      !name.equals(DefaultChatTierName.LOCAL) &&
      !name.equals(DefaultChatTierName.WHISPER)) {
      this.channels.remove(name);
      ConfigFile.deleteChatTier(name);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Collection<String> getChannels() {
    return Collections.unmodifiableCollection(this.channels.keySet());
  }

  @Override
  public void loadDefaults() {
    String globalName = DefaultChatTierName.GLOBAL;
    String shoutName = DefaultChatTierName.SHOUT;
    String localName = DefaultChatTierName.LOCAL;
    String whisperName = DefaultChatTierName.WHISPER;

    Map<String, Object> globalMetadata = MapUtil.map(Metadata.TYPE, ChatTierType.GLOBAL);
    Map<String, Object> shoutMetadata = MapUtil.map(Metadata.TYPE, ChatTierType.GLOBAL);
    Map<String, Object> localMetadata = MapUtil.map(Metadata.TYPE, ChatTierType.RANGED, Metadata.RANGE, ConfigFile.localChannelRange);
    Map<String, Object> whisperMetadata = MapUtil.map(Metadata.TYPE, ChatTierType.RANGED, Metadata.RANGE, ConfigFile.whisperChannelRange);

    this.channels.put(globalName, globalMetadata);
    this.channels.put(shoutName, shoutMetadata);
    this.channels.put(localName, localMetadata);
    this.channels.put(whisperName, whisperMetadata);
  }

  @Override
  public void loadCustoms() {
    ConfigFile.loadChatTiers()
      .forEach(metadata -> {
        String name = (String) metadata.get(Metadata.NAME);
        metadata.remove(Metadata.NAME);
        this.channels.put(name, metadata);
      });
  }
}
