package com.outlook.furkan.dogan.dev.ohachat.common.constant;

import java.util.regex.Pattern;

/**
 * @author Furkan Doğan
 */
public interface NamePatterns {

  Pattern CHAT_TIER_NAME = Pattern.compile("[a-zA-Z\\d]+");
}
