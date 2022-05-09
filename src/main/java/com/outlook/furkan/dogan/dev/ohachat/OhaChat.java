package com.outlook.furkan.dogan.dev.ohachat;

import com.outlook.furkan.dogan.dev.ohachat.common.config.ConfigFile;
import com.outlook.furkan.dogan.dev.ohachat.common.constant.DefaultChatTierName;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql.impl.SQLite;
import com.outlook.furkan.dogan.dev.ohachat.handler.ChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.handler.DefaultChatHandler;
import com.outlook.furkan.dogan.dev.ohachat.listener.ChatListener;
import com.outlook.furkan.dogan.dev.ohachat.manager.ChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.DefaultChatTierManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.DefaultPreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.manager.PreferencesManager;
import com.outlook.furkan.dogan.dev.ohachat.processor.ChatTierProcessor;
import com.outlook.furkan.dogan.dev.ohachat.processor.DefaultChatTierProcessor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@SuppressWarnings("unused")
public final class OhaChat extends JavaPlugin {

  @Override
  public void onEnable() {
    this.loadConfig();
    DefaultChatTierName.init();

    File dataFile = new File(this.getDataFolder(), "data.db");
    DataSource dataSource = new SQLite(dataFile);
    dataSource.loadAll();

    ChatTierManager chatTierManager = new DefaultChatTierManager(dataSource);
    PreferencesManager preferencesManager = new DefaultPreferencesManager(dataSource);
    ChatTierProcessor chatTierProcessor = new DefaultChatTierProcessor(preferencesManager);

    this.registerChatListener(chatTierManager, chatTierProcessor);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  private void loadConfig() {
    ConfigFile.loadFile(this);
  }

  private void registerChatListener(ChatTierManager chatTierManager,
                                    ChatTierProcessor chatTierProcessor) {
    ChatHandler chatHandler = new DefaultChatHandler(chatTierManager, chatTierProcessor);
    ChatListener chatListener = new ChatListener(chatHandler);

    this.getServer().getPluginManager().registerEvents(chatListener, this);
  }
}
