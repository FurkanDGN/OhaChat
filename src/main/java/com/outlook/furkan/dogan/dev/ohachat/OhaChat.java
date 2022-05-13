package com.outlook.furkan.dogan.dev.ohachat;

import com.outlook.furkan.dogan.dev.ohachat.command.BlockCommand;
import com.outlook.furkan.dogan.dev.ohachat.command.ChannelCommand;
import com.outlook.furkan.dogan.dev.ohachat.command.OhaAdminCommand;
import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;
import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.Metadata;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql.impl.SQLite;
import com.outlook.furkan.dogan.dev.ohachat.handler.ChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.CommandHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.DefaultChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.DefaultCommandHandler;
import com.outlook.furkan.dogan.dev.ohachat.hook.PAPIHook;
import com.outlook.furkan.dogan.dev.ohachat.listener.ChatListener;
import com.outlook.furkan.dogan.dev.ohachat.manager.*;
import com.outlook.furkan.dogan.dev.ohachat.processor.ChatTierProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.CommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.DefaultChatTierProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.DefaultCommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.util.NmsCommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@SuppressWarnings("unused")
public final class OhaChat extends JavaPlugin {

  @Override
  public void onEnable() {
    File dataFile = new File(this.getDataFolder(), "data.db");
    DataSource dataSource = new SQLite(dataFile);
    ChatTierManager chatTierManager = new DefaultChatTierManager(dataSource);
    PreferencesManager preferencesManager = new DefaultPreferencesManager(dataSource);
    CommandProcessor commandProcessor = new DefaultCommandProcessor(chatTierManager);
    CommandHandler commandHandler = new DefaultCommandHandler(commandProcessor);
    ChatTierCommandManager chatTierCommandManager = new DefaultChatTierCommandManager(commandHandler);

    this.loadConfigurationFiles();
    DefaultChatTierName.loadFromConfig();
    dataSource.loadAll();
    chatTierManager.loadDefaults();
    chatTierManager.loadCustoms();

    this.loadPlugin(chatTierManager, commandHandler, preferencesManager, chatTierCommandManager);
  }

  private void loadConfigurationFiles() {
    ConfigFile.loadFile(this);
    LanguageFile.loadFile(this);
  }

  private void loadPlugin(ChatTierManager chatTierManager,
                          CommandHandler commandHandler,
                          PreferencesManager preferencesManager,
                          ChatTierCommandManager chatTierCommandManager) {
    ChatTierProcessor chatTierProcessor = new DefaultChatTierProcessor(preferencesManager);
    ChatHandler chatHandler = new DefaultChatHandler(chatTierManager, chatTierProcessor);
    ChatListener chatListener = new ChatListener(chatHandler);

    this.getServer().getPluginManager().registerEvents(chatListener, this);

    this.registerCommands(commandHandler, chatTierManager, preferencesManager, chatTierCommandManager);

    Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
    if (placeholderAPI != null) {
      new PAPIHook(chatTierManager).register();
    }
  }

  @SuppressWarnings("ConstantConditions")
  private void registerCommands(CommandHandler commandHandler,
                                ChatTierManager chatTierManager,
                                PreferencesManager preferencesManager,
                                ChatTierCommandManager chatTierCommandManager) {
    String global = DefaultChatTierName.GLOBAL;
    String shout = DefaultChatTierName.SHOUT;
    String local = DefaultChatTierName.LOCAL;
    String whisper = DefaultChatTierName.WHISPER;

    ChannelCommand channelCommand = new ChannelCommand("channel", commandHandler);
    ChannelCommand globalCommand = new ChannelCommand(global, commandHandler);
    ChannelCommand shoutCommand = new ChannelCommand(shout, commandHandler);
    ChannelCommand localCommand = new ChannelCommand(local, commandHandler);
    ChannelCommand whisperCommand = new ChannelCommand(whisper, commandHandler);

    NmsCommandUtil.registerCommand("channel", channelCommand);
    NmsCommandUtil.registerCommand(global, globalCommand);
    NmsCommandUtil.registerCommand(shout, shoutCommand);
    NmsCommandUtil.registerCommand(local, localCommand);
    NmsCommandUtil.registerCommand(whisper, whisperCommand);

    OhaAdminCommand ohaAdminCommand = new OhaAdminCommand(chatTierManager, chatTierCommandManager);
    this.getCommand("ohaadmin").setExecutor(ohaAdminCommand);

    BlockCommand blockCommand = new BlockCommand(preferencesManager);
    this.getCommand("block").setExecutor(blockCommand);
    this.getCommand("unblock").setExecutor(blockCommand);

    ConfigFile.loadChatTiers()
      .forEach(metadata -> {
        String name = (String) metadata.get(Metadata.NAME);
        chatTierCommandManager.registerCommand(name);
      });
  }
}
