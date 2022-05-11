package com.outlook.furkan.dogan.dev.ohachat.manager;

/**
 * @author Furkan Doğan
 */
public interface ChatTierCommandManager {

  void registerCommand(String name);

  void unregisterCommand(String name);
}
