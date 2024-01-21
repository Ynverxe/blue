package com.github.ynverxe.blue.sql.credential;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class SQLCredential {

  private final @NotNull String ip;
  private final @NotNull String port;
  private final @NotNull String database;
  private final @NotNull String username;
  private final @NotNull String password;

  public SQLCredential(@NotNull String ip, @NotNull String port, @NotNull String database, @NotNull String username, @NotNull String password)
    throws IllegalArgumentException {
    this.ip = ip;
    if (ip.isEmpty()) {
      throw new IllegalArgumentException("Ip field is empty");
    }
    this.port = port;
    if (port.isEmpty()) {
      throw new IllegalArgumentException("Port field is empty");
    }
    this.database = database;
    if (database.isEmpty()) {
      throw new IllegalArgumentException("Database name field is empty");
    }
    this.username = username;
    this.password = password;
  }

  public String ip() {
    return ip;
  }

  public String port() {
    return port;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public String formatJdbcUrl(@NotNull String url) {
    return String.format(url, ip, port);
  }

  public Map<String, Object> toMap() {
    Map<String, Object> objectMap = new LinkedHashMap<>();
    objectMap.put("ip", ip);
    objectMap.put("port", port);
    objectMap.put("database", database);
    objectMap.put("username", username);
    objectMap.put("password", password);
    return objectMap;
  }

  public Properties toProperties() {
    Properties properties = new Properties();
    properties.putAll(toMap());
    return properties;
  }

  public static @NotNull SQLCredential fromMap(@NotNull Map<String, Object> objectMap) {
    return new SQLCredential(
      (String) objectMap.getOrDefault("ip", ""),
      (String) objectMap.getOrDefault("port", ""),
      (String) objectMap.getOrDefault("database", ""),
      (String) objectMap.getOrDefault("username", ""),
      (String) objectMap.getOrDefault("password", ""));
  }

  public static @NotNull SQLCredential fromProperties(@NotNull Properties properties) {
    Map<String, Object> objectMap = new LinkedHashMap<>();
    properties.forEach((key, value) -> objectMap.put(key.toString(), value));
    return fromMap(objectMap);
  }
}