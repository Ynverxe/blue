package com.github.ynverxe.blue.sql.vendor;

import org.jetbrains.annotations.NotNull;

/**
 * Contains an SQL Vendor Information.
 */
public interface Vendor {

  @NotNull String jdbcUrl();

}