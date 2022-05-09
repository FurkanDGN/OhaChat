package com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql.impl;

import com.outlook.furkan.dogan.dev.ohachat.common.datasource.sql.SQLSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SQLite extends SQLSource {

  public SQLite(File file) {
    super(file);
  }

  @Override
  protected Connection createConnection() {
    if (!this.file.exists()) {
      try {
        this.file.getParentFile().mkdir();
        this.file.createNewFile();
      } catch (final Exception ignored) {
      }
    }

    try {
      Class.forName("org.sqlite.JDBC");
      return DriverManager.getConnection("jdbc:sqlite:" + this.file);
    } catch (final ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
