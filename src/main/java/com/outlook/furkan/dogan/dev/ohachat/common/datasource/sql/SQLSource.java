package com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql;

import com.gmail.furkanaxx34.dlibrary.replaceable.RpString;
import com.outlook.furkan.dogan.dev.ohachat.common.datasource.DataSource;
import com.outlook.furkan.dogan.dev.ohachat.common.domain.OhaPlayer;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

public abstract class SQLSource extends DataSource {

  private static final String TABLE_NAME = "oha_players";

  protected File file;

  private final Connection connection;

  public SQLSource(File file) {
    this.file = file;
    this.connection = this.createConnection();
    this.createTables();
  }

  @Override
  protected synchronized Set<OhaPlayer> getAll() {
    Set<OhaPlayer> ohaPlayers = new HashSet<>();
    String statement = Query.SELECT_ALL.build(new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME));

    try (PreparedStatement pt = this.connection.prepareStatement(statement)) {
      ResultSet resultSet = pt.executeQuery();
      while (resultSet.next()) {
        UUID uniqueId = UUID.fromString(resultSet.getString("unique_id"));
        String blacklistString = resultSet.getString("blacklist");
        Set<String> blacklist = blacklistString.isEmpty() ? new HashSet<>() :
          Arrays.stream(blacklistString.split("\\s*,\\s*"))
            .collect(Collectors.toSet());
        String channel = resultSet.getString("channel");

        OhaPlayer ohaPlayer = new OhaPlayer(uniqueId, blacklist, channel);
        ohaPlayers.add(ohaPlayer);
      }

      resultSet.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return ohaPlayers;
  }

  @Override
  protected synchronized void saveSupply(OhaPlayer ohaPlayer) {
    try {
      UUID uuid = ohaPlayer.getUniqueId();
      Set<String> blacklist = ohaPlayer.getBlacklist();
      String blackListString = String.join(",", blacklist);
      String channel = ohaPlayer.getChannel();

      if (this.playerNotExists(uuid)) {
        this.insert(ohaPlayer);
      } else {
        this.updateColumn("blacklist", uuid, blackListString);
        this.updateColumn("channel", uuid, channel);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  protected synchronized void removeSupply(UUID uuid) {
    try {
      this.delete(uuid);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  protected abstract Connection createConnection();

  private void delete(UUID uuid) throws SQLException {
    final PreparedStatement statement = this.connection.prepareStatement(Query.DELETE.build(
      new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME)
    ));
    statement.setString(1, uuid.toString());
    statement.execute();
    statement.close();
  }

  private void createTables() {
    try {
      this.connection.createStatement().execute(
        Query.CREATE_TABLE.build(new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME))
      );
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void updateColumn(String column, UUID uniqueId, Object object) throws SQLException {
    PreparedStatement statement = this.connection.prepareStatement(Query.UPDATE.build(
      new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME),
      new SimpleEntry<>("%column", () -> column)
    ));
    statement.setObject(1, object);
    statement.setString(2, uniqueId.toString());
    statement.executeUpdate();
    statement.close();
  }

  private void insert(OhaPlayer player) throws SQLException {
    PreparedStatement statement = this.connection.prepareStatement(Query.INSERT.build(
      new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME)
    ));
    final UUID uniqueId = player.getUniqueId();
    String uniqueIdString = uniqueId.toString();
    Set<String> blacklist = player.getBlacklist();
    String blackListString = String.join(",", blacklist);
    String channel = player.getChannel();

    statement.setString(1, uniqueIdString);
    statement.setString(2, blackListString);
    statement.setString(3, channel);
    statement.executeUpdate();
    statement.close();
  }

  private boolean playerNotExists(UUID uuid) {
    try {
      PreparedStatement statement = this.connection.prepareStatement(Query.SELECT.build(
        new SimpleEntry<>("%identifier", () -> "*"),
        new SimpleEntry<>("%table", () -> SQLSource.TABLE_NAME)
      ));
      statement.setString(1, uuid.toString());
      ResultSet resultSet = statement.executeQuery();
      boolean b = !resultSet.next();
      resultSet.close();
      return b;
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return true;
  }

  public static final class Query {

    public static RpString CREATE_TABLE = RpString.from(
      "CREATE TABLE IF NOT EXISTS %table (" +
        "unique_id VARCHAR(70) NOT NULL," +
        "blacklist VARCHAR(9999) NOT NULL," +
        "channel VARCHAR(9999) NOT NULL," +
        "UNIQUE (unique_id));"
    ).regex("%table");

    public static RpString INSERT = RpString.from(
      "INSERT INTO %table (" +
        " unique_id," +
        " blacklist," +
        " channel" +
        ") VALUES (?, ?, ?);"
    ).regex("%table");

    public static RpString SELECT_ALL = RpString.from(
      "SELECT * FROM %table;"
    ).regex("%table");

    public static RpString SELECT_WHERE = RpString.from(
      "SELECT %identifier FROM %table WHERE %where;"
    ).regex("%identifier", "%table", "%where");

    public static RpString SELECT = RpString.from(
      "SELECT %identifier FROM %table WHERE unique_id=?;"
    ).regex("%identifier", "%table");

    public static RpString UPDATE = RpString.from(
      "UPDATE %table SET %column=? WHERE unique_id=?;"
    ).regex("%table", "%column");

    public static RpString DELETE = RpString.from(
      "DELETE FROM %table WHERE unique_id=?;"
    ).regex("%table");
  }
}
