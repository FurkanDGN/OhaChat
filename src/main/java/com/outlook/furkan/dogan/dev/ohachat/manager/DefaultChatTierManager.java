package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierMetadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.OhaPlayer;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
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

    this.initiate();
  }

  @Override
  public ChatTier findChatTier(Player player) {
    UUID uniqueId = player.getUniqueId();
    OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);
    String channel = ohaPlayer.getChannel();

    return Optional.of(channel)
      .map(this.chatTiers::get)
      .orElse(this.chatTiers.get(DefaultChatTierName.GLOBAL));
  }

  @Override
  public boolean setChatTier(Player player, String channel) {
    ChatTier chatTier = this.chatTiers.get(channel);

    if (chatTier == null) {
      return false;
    } else {
      UUID uniqueId = player.getUniqueId();
      OhaPlayer ohaPlayer = this.dataSource.getPlayer(uniqueId);

      ohaPlayer.setChannel(channel);
      this.dataSource.save(ohaPlayer);

      return true;
    }
  }

  private void initiate() {
    String globalName = DefaultChatTierName.GLOBAL;
    String shoutName = DefaultChatTierName.SHOUT;
    String localName = DefaultChatTierName.LOCAL;
    String whisperName = DefaultChatTierName.WHISPER;

    ChatTier global = new ChatTier(globalName, ChatTierType.GLOBAL, Collections.emptyMap());
    ChatTier shout = new ChatTier(shoutName, ChatTierType.SHOUT, Collections.emptyMap());
    ChatTier local = new ChatTier(localName, ChatTierType.LOCAL, MapUtil.map(ChatTierMetadata.RANGE, 20.0D));
    ChatTier whisper = new ChatTier(whisperName, ChatTierType.WHISPER, MapUtil.map(ChatTierMetadata.RANGE, 3.0D));

    this.chatTiers.put(globalName, global);
    this.chatTiers.put(shoutName, shout);
    this.chatTiers.put(localName, local);
    this.chatTiers.put(whisperName, whisper);
  }
}
