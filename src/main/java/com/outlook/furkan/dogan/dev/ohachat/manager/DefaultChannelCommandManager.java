package com.outlook.furkan.dogan.dev.ohachat.manager;

import com.outlook.furkan.dogan.dev.ohachat.command.ChannelCommand;
import com.outlook.furkan.dogan.dev.ohachat.handler.CommandHandler;
import com.outlook.furkan.dogan.dev.ohachat.util.NmsCommandUtil;

/**
 * @author Furkan Doğan
 */
public class DefaultChannelCommandManager implements ChannelCommandManager {

  private final CommandHandler commandHandler;

  public DefaultChannelCommandManager(CommandHandler commandHandler) {
    this.commandHandler = commandHandler;
  }

  @Override
  public void registerCommand(String name) {
    ChannelCommand command = new ChannelCommand(name, this.commandHandler);
    NmsCommandUtil.registerCommand(name, command);
  }

  @Override
  public void unregisterCommand(String name) {
    NmsCommandUtil.unregisterCommand(name);
  }
}
