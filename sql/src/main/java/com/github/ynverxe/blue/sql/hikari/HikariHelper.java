package com.github.ynverxe.blue.sql.hikari;

import com.github.ynverxe.blue.sql.credential.SQLCredential;
import com.github.ynverxe.blue.sql.vendor.KnownVendor;
import com.github.ynverxe.blue.sql.vendor.Vendor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.util.PropertyElf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class HikariHelper {

  private static final @NotNull HikariHelper INSTANCE = new HikariHelper(HikariConfig::new);

  private final @NotNull Supplier<HikariConfig> hikariConfigBaseSupplier;

  public HikariHelper(@NotNull Supplier<HikariConfig> hikariConfigBaseSupplier) {
    this.hikariConfigBaseSupplier = hikariConfigBaseSupplier;
  }

  public HikariHelper(@NotNull Properties properties) {
    this(() -> new HikariConfig(properties));
  }

  public HikariConfig newBase() {
    return hikariConfigBaseSupplier.get();
  }

  public HikariConfig setup(@NotNull SQLCredential credential, @NotNull String jdbcUrl) {
    HikariConfig hikariConfig = newBase();
    hikariConfig.setJdbcUrl(credential.formatJdbcUrl(jdbcUrl));
    hikariConfig.setUsername(credential.username());
    hikariConfig.setPassword(credential.password());
    return hikariConfig;
  }

  public HikariConfig setup(@NotNull SQLCredential credential, @NotNull Vendor vendor) {
    return setup(credential, vendor.jdbcUrl());
  }

  public HikariConfig setup(@NotNull SQLCredential credential, @NotNull KnownVendor knownVendor) {
    return setup(credential, knownVendor.jdbcUrl());
  }

  public HikariConfig setupFromMap(@NotNull Map<String, Object> credentialMap, @NotNull Vendor vendor) {
    return setup(SQLCredential.fromMap(credentialMap), vendor);
  }

  public HikariConfig setupFromMap(@NotNull Map<String, Object> credentialMap, @NotNull KnownVendor knownVendor) {
    return setupFromMap(credentialMap, (Vendor) knownVendor);
  }

  public HikariConfig fullySetupFromMap(@NotNull Map<String, Object> credentialMap) {
    return setupFromMap(credentialMap, KnownVendor.byName((String) credentialMap.getOrDefault("vendor", "missing-vendor")));
  }

  public static HikariHelper getInstance() {
    return INSTANCE;
  }
}