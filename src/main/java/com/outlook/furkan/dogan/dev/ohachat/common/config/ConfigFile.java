package com.outlook.furkan.dogan.dev.ohachat.common.config;

import com.gmail.furkanaxx34.dlibrary.bukkit.transformer.resolvers.BukkitSnakeyaml;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformedObject;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformerPool;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Comment;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Exclude;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Names;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierConfigPath;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.NamePatterns;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.ChatTier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * @author Furkan Doğan
 */
@Names(modifier = Names.Modifier.TO_LOWER_CASE, strategy = Names.Strategy.HYPHEN_CASE)
public class ConfigFile extends TransformedObject {

  @Exclude
  private static TransformedObject instance;

  @Comment("Name of global channel")
  public static String globalChannelName = "global";

  @Comment("Name of shout channel")
  public static String shoutChannelName = "shout";

  @Comment("Name of local channel")
  public static String localChannelName = "local";

  @Comment("Range of local channel")
  public static double localChannelRange = 20.0D;

  @Comment("Name of whisper channel")
  public static String whisperChannelName = "whisper";

  @Comment("Range of whisper channel")
  public static double whisperChannelRange = 3.0D;

  private ConfigFile() {
  }

  public static void loadFile(final Plugin plugin) {
    if (ConfigFile.instance == null) {
      ConfigFile.instance = TransformerPool.create(new ConfigFile())
        .withFile(new File(plugin.getDataFolder(), "config.yml"))
        .withResolver(new BukkitSnakeyaml());
    }

    ConfigFile.instance.initiate();
  }

  public static void saveChatTier(ChatTier chatTier) {
    String name = chatTier.getName();
    String chatTierType = chatTier.getChatTierType().name();
    Map<String, Object> metadata = chatTier.getMetadata();

    ConfigFile.saveChatTierType(name, chatTierType);
    ConfigFile.saveChatTierMetadata(name, metadata);

    ConfigFile.instance.save();
  }

  public static void deleteChatTier(String name) {
    String path = String.format(ChatTierConfigPath.NAME_PATH, name);

    ConfigFile.instance.remove(path);
    ConfigFile.instance.save();
  }

  public static Set<ChatTier> loadChatTiers() {
    Set<ChatTier> chatTiers = new HashSet<>();

    for (String key : ConfigFile.instance.getAllKeys()) {
      if (!key.equals(ChatTierConfigPath.PARENT_PATH)) continue;
      if (!NamePatterns.CHAT_TIER_NAME.matcher(key).matches()) continue;

      String typePath = String.format(ChatTierConfigPath.TYPE_PATH, key);
      String metadataSectionPath = String.format(ChatTierConfigPath.METADATA_PATH, key);

      ChatTierType chatTierType = ConfigFile.buildChatTierType(typePath);
      if (chatTierType == null) continue;

      Map<String, Object> metadata = ConfigFile.buildMetadata(metadataSectionPath);

      ChatTier chatTier = new ChatTier(key, chatTierType, metadata);
      chatTiers.add(chatTier);
    }

    return chatTiers;
  }

  private static void saveChatTierType(String name, String type) {
    String typePath = String.format(ChatTierConfigPath.TYPE_PATH, name);
    ConfigFile.instance.set(typePath, type);
  }

  private static void saveChatTierMetadata(String name, Map<String, Object> metadata) {
    metadata.forEach((key, value) -> {
      String metadataPath = String.format(ChatTierConfigPath.METADATA_PATH, name);
      String metadataValuePath = String.format("%s.%s", metadataPath, key);
      ConfigFile.instance.set(metadataValuePath, value);
    });
  }

  private static ChatTierType buildChatTierType(String path) {
    String type = String.valueOf(ConfigFile.instance.get(path).orElse(null));
    return ChatTierType.fromString(type);
  }

  private static Map<String, Object> buildMetadata(String path) {
    ConfigurationSection section = (ConfigurationSection) ConfigFile.instance.get(path).orElse(null);
    if (section != null) {
      return section.getKeys(false)
        .stream()
        .map(key -> {
          String metadataPath = String.format("%s.%s", path, key);
          Object value = ConfigFile.instance.get(metadataPath).orElse(null);
          return new SimpleEntry<>(key, value);
        })
        .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    } else {
      return Collections.emptyMap();
    }
  }
}
