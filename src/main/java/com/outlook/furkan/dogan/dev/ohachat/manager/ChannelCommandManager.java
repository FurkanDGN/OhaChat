package com.outlook.furkan.dogan.dev.ohachat.manager;

/**
 * @author Furkan Doğan
 */
public interface ChannelCommandManager {

  void registerCommand(String name);

  void unregisterCommand(String name);
}
