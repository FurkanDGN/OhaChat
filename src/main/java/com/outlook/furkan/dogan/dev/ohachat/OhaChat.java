package com.outlook.furkan.dogan.dev.ohachat;

import com.outlook.furkan.dogan.dev.ohachat.command.ChannelCommand;
import com.outlook.furkan.dogan.dev.ohachat.command.OhaAdminCommand;
import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;
import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql.impl.SQLite;
import com.outlook.furkan.dogan.dev.ohachat.handler.ChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.CommandHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.DefaultChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.DefaultCommandHandler;
import com.outlook.furkan.dogan.dev.ohachat.listener.ChatListener;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.DefaultChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.DefaultPreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.processor.ChatTierProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.CommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.DefaultChatTierProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.DefaultCommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.util.NmsCommandUtil;
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
    ChatTierProcessor chatTierProcessor = new DefaultChatTierProcessor(preferencesManager);
    CommandProcessor commandProcessor = new DefaultCommandProcessor(chatTierManager);

    this.loadConfig();
    DefaultChatTierName.loadFromConfig();
    dataSource.loadAll();
    chatTierManager.loadDefaults();
    this.loadPlugin(chatTierManager, chatTierProcessor, commandProcessor);
  }

  private void loadConfig() {
    ConfigFile.loadFile(this);
    LanguageFile.loadFile(this);
  }

  private void loadPlugin(ChatTierManager chatTierManager,
                          ChatTierProcessor chatTierProcessor,
                          CommandProcessor commandProcessor) {
    ChatHandler chatHandler = new DefaultChatHandler(chatTierManager, chatTierProcessor);
    CommandHandler commandHandler = new DefaultCommandHandler(commandProcessor);
    ChatListener chatListener = new ChatListener(chatHandler);

    this.getServer().getPluginManager().registerEvents(chatListener, this);

    this.registerCommands(commandHandler, chatTierManager);
  }

  @SuppressWarnings("ConstantConditions")
  private void registerCommands(CommandHandler commandHandler,
                                ChatTierManager chatTierManager) {
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

    OhaAdminCommand ohaAdminCommand = new OhaAdminCommand(chatTierManager);
    this.getCommand("ohaadmin").setExecutor(ohaAdminCommand);
  }
}
