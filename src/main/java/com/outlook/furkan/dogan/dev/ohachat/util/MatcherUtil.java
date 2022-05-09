package com.outlook.furkan.dogan.dev.ohachat.util;

import java.util.regex.Matcher;

/**
 * @author Furkan Doğan
 */
public class MatcherUtil {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static boolean groupExists(Matcher matcher, String group) {
    try {
      matcher.group(group);
      return true;
    } catch (IllegalArgumentException ignored) {
      return false;
    }
  }
}
