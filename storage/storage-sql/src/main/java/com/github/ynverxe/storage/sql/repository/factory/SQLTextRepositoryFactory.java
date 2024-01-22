package com.github.ynverxe.storage.sql.repository.factory;

import com.github.ynverxe.blue.sql.credential.SQLCredential;
import com.github.ynverxe.blue.sql.vendor.KnownVendor;
import com.github.ynverxe.blue.storage.crud.repository.factory.CrudRepositoryFactory;
import com.github.ynverxe.blue.storage.crud.repository.factory.context.CrudRepositoryParameters;
import com.github.ynverxe.storage.sql.repository.SQLTextRepository;
import com.github.ynverxe.storage.sql.query.ParameterizedQueryFactory;
import com.github.ynverxe.storage.sql.source.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class SQLTextRepositoryFactory implements CrudRepositoryFactory<SQLTextRepository> {

  private final @NotNull ParameterizedQueryFactory queryFactory;

  public SQLTextRepositoryFactory(@NotNull ParameterizedQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull SQLTextRepository createRepository(@NotNull CrudRepositoryParameters parameters) throws SQLException {
    String jdbcUrl = (String) parameters.get("jdbcUrl");

    if (jdbcUrl == null) {
      String vendorName = (String) parameters.get("vendor-name");

      if (vendorName != null) {
        jdbcUrl = KnownVendor.byName(vendorName).jdbcUrl();
      }
    }

    if (jdbcUrl == null)
      throw new IllegalArgumentException("Cannot construct an appropriate jdbcUrl ");

    SQLCredential credential = (SQLCredential) parameters.get("credential");
    if (credential == null) {
      Object credentialData = parameters.get("credential-data");

      if (credentialData instanceof Map) {
        credential = SQLCredential.fromMap((Map<String, Object>) credentialData);
      }
    }

    if (credential == null)
      throw new IllegalArgumentException("Cannot construct an appropriate SQLCredential");

    ConnectionSource connectionSource = establishConnection(credential, jdbcUrl);
    return new SQLTextRepository(queryFactory, connectionSource);
  }

  protected ConnectionSource establishConnection(SQLCredential credential, String jdbcUrl) throws SQLException {
    return new ConnectionSource(DriverManager.getConnection(credential.formatJdbcUrl(jdbcUrl), credential.username(), credential.password()));
  }
}