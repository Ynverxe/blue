package com.github.ynverxe.blue.sql.vendor;

import org.jetbrains.annotations.NotNull;

/**
 * A list of most known SQL vendors.
 */
public enum KnownVendor implements Vendor {

  MYSQL("jdbc:mysql://<ip>:<port>/<db>"),
  MARIA_DB("jdbc:mariadb://<ip>:<port>/<db>"),
  POSTGRESQL("jdbc:postgresql://<ip>:<port>/<db>"),
  H2_EMBEDDED("jdbc:h2://<ip>:<port>/<db>");

  private final String jdbcUrl;

  KnownVendor(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  @Override
  public @NotNull String jdbcUrl() {
    return jdbcUrl;
  }

  public static @NotNull KnownVendor byName(@NotNull String name) {
    return valueOf(name.toUpperCase());
  }
}