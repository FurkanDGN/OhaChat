package com.outlook.furkan.dogan.dev.ohachat.handler;

import com.outlook.furkan.dogan.dev.ohachat.processor.CommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.util.MatcherUtil;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Furkan Doğan
 */
public class DefaultCommandHandler implements CommandHandler {

  private static final Pattern COMMAND_PATTERN = Pattern.compile("(^/)((channel\\s)?(?<channel>\\w+)(\\s(?<message>.*))?)");

  private final CommandProcessor commandProcessor;

  public DefaultCommandHandler(CommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
  }

  @Override
  public boolean handle(Player player, String input) {
    Matcher matcher = DefaultCommandHandler.COMMAND_PATTERN.matcher(input);

    if (matcher.matches()) {
      String channel = matcher.group("channel");
      String message = MatcherUtil.groupExists(matcher, "message") ? matcher.group("message") : null;

      return this.commandProcessor.process(player, channel, message);
    } else {
      return false;
    }
  }
}
