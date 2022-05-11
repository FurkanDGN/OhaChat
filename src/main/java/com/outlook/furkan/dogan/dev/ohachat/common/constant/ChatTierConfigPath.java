package com.outlook.furkan.dogan.dev.ohachat.common.constant;

/**
 * @author Furkan Doğan
 */
public interface ChatTierConfigPath {

  String PARENT_PATH = "custom";

  String NAME_PATH = String.format("%s.%s", ChatTierConfigPath.PARENT_PATH, "%s");

  String TYPE_PATH = String.format("%s.type", ChatTierConfigPath.NAME_PATH);

  String METADATA_PATH = String.format("%s.metadata", ChatTierConfigPath.NAME_PATH);
}
