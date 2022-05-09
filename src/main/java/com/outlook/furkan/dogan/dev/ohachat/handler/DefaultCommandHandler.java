package com.outlook.furkan.dogan.dev.ohachat.handler;

import com.outlook.furkan.dogan.dev.ohachat.common.config.LanguageFile;
import com.outlook.furkan.dogan.dev.ohachat.processor.CommandProcessor;
import com.outlook.furkan.dogan.dev.ohachat.util.MatcherUtil;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
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
      boolean startsWith = input.startsWith("/channel");
      String channel = matcher.group("channel");
      String message = MatcherUtil.groupExists(matcher, "message") ? matcher.group("message") : null;

      boolean process = this.commandProcessor.process(player, channel, message);

      if (!process && startsWith) {
        String errorMessage = LanguageFile.channelNotFound.build(
          new SimpleEntry<>("%channel%", () -> channel)
        );

        player.sendMessage(errorMessage);
        return false;
      }

      return process;
    } else {
      return false;
    }
  }
}
