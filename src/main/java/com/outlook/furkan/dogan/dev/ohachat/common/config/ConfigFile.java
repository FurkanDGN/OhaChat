package com.outlook.furkan.dogan.dev.ohachat.common.config;

import com.gmail.furkanaxx34.dlibrary.bukkit.transformer.resolvers.BukkitSnakeyaml;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformedObject;
import com.gmail.furkanaxx34.dlibrary.transformer.TransformerPool;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Comment;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Exclude;
import com.gmail.furkanaxx34.dlibrary.transformer.annotations.Names;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.ChatTierConfigPath;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.Metadata;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.NamePatterns;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.chat.tier.ChatTierType;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.exception.InvalidTypeException;
import com.outlook.furkan.dogan.dev.ohachat.util.MapUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Furkan Doğan
 */
@Names(modifier = Names.Modifier.TO_LOWER_CASE, strategy = Names.Strategy.HYPHEN_CASE)
public class ConfigFile extends TransformedObject {

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

  @Exclude
  private static TransformedObject instance;

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

  public static void saveChatTier(String channelName, Map<String, Object> metadata) {
    String chatTierType = ((ChatTierType) metadata.get(Metadata.TYPE)).getName();

    ConfigFile.saveChatTierType(channelName, chatTierType);
    ConfigFile.saveChatTierMetadata(channelName, metadata);

    ConfigFile.instance.save();
  }

  public static void deleteChatTier(String name) {
    String path = String.format(ChatTierConfigPath.NAME_PATH, name);

    ConfigFile.instance.remove(path);
    ConfigFile.instance.save();
  }

  public static Set<Map<String, Object>> loadChatTiers() {
    return ConfigFile.instance.get(ChatTierConfigPath.PARENT_PATH)
      .map(o -> {
        ConfigurationSection section = (ConfigurationSection) o;

        return section.getKeys(false)
          .stream()
          .filter(key -> NamePatterns.CHAT_TIER_NAME.matcher(key).matches())
          .map(name -> {
            String typePath = String.format(ChatTierConfigPath.TYPE_PATH, name);
            String metadataSectionPath = String.format(ChatTierConfigPath.METADATA_PATH, name);

            Map<String, Object> metadata = ConfigFile.buildMetadata(metadataSectionPath);
            String type = (String) ConfigFile.instance.get(typePath).orElseThrow(() -> new InvalidTypeException("Incorrect configuration [" + name + "]"));
            switch (type) {
              case "global": {
                return MapUtil.<String, Object>map(Metadata.NAME, name, Metadata.TYPE, ChatTierType.GLOBAL);
              }
              case "world": {
                return MapUtil.<String, Object>map(Metadata.NAME, name, Metadata.TYPE, ChatTierType.WORLD);
              }
              case "ranged": {
                Object rangeObject = metadata.get(Metadata.RANGE);
                if (rangeObject == null) {
                  throw new RuntimeException(Metadata.RANGE + " is required for ranged chat tier");
                }

                double range = Double.parseDouble(String.valueOf(rangeObject));
                return MapUtil.<String, Object>map(Metadata.NAME, name, Metadata.TYPE, ChatTierType.RANGED, Metadata.RANGE, range);
              }
              default:
                throw new InvalidTypeException("Incorrect configuration [" + name + "]");
            }
          })
          .collect(Collectors.toSet());
      })
      .orElse(Collections.emptySet());
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
