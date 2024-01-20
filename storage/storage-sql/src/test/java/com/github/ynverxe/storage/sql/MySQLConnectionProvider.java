package com.github.ynverxe.storage.sql;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnectionProvider {

  public static final Connection CONNECTION;

  static {
    CONNECTION = connectWithH2TestDB();
  }

  private static Connection connectWithH2TestDB() {
    try {
      Properties credentials = new Properties();
      File credentialsFile = Paths.get("src/test/resources/mysql-test-credentials.properties")
        .toFile();

      try (FileReader reader = new FileReader(credentialsFile)) {
        credentials.load(reader);
      } catch (Exception e) {
        return null;
      }

      String database = credentials.getProperty("database");
      String ip = credentials.getProperty("ip");
      String port = credentials.getProperty("port");
      String user = credentials.getProperty("user");
      String password = credentials.getProperty("password");

      return DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", ip, port, database), user, password);
    } catch (SQLException e) {
      return null;
    }
  }
}