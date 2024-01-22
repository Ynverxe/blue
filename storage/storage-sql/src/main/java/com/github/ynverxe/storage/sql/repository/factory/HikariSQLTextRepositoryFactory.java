package com.github.ynverxe.storage.sql.repository.factory;

import com.github.ynverxe.blue.sql.credential.SQLCredential;
import com.github.ynverxe.blue.sql.hikari.HikariHelper;
import com.github.ynverxe.storage.sql.repository.query.ParameterizedQueryFactory;
import com.github.ynverxe.storage.sql.repository.source.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class HikariSQLTextRepositoryFactory extends SQLTextRepositoryFactory {

  private final HikariHelper hikariHelper;

  public HikariSQLTextRepositoryFactory(@NotNull ParameterizedQueryFactory queryFactory, HikariHelper hikariHelper) {
    super(queryFactory);
    this.hikariHelper = hikariHelper;
  }

  @Override
  @SuppressWarnings("all")
  protected ConnectionSource establishConnection(SQLCredential credential, String jdbcUrl) throws SQLException {
    HikariConfig config = hikariHelper.setup(credential, jdbcUrl);
    HikariDataSource dataSource = new HikariDataSource(config);
    return new ConnectionSource(dataSource.getConnection(), dataSource);
  }
}