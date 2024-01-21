package com.github.ynverxe.storage.sql.repository;

import com.github.ynverxe.blue.storage.crud.repository.types.TextCrudRepository;
import com.github.ynverxe.storage.sql.repository.query.ParameterizedQueryFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SQLTextRepository implements TextCrudRepository {

  private final @NotNull ParameterizedQueryFactory queryFactory;
  private final @NotNull Connection connection;

  public SQLTextRepository(@NotNull ParameterizedQueryFactory queryFactory, @NotNull Connection connection) {
    this.queryFactory = queryFactory;
    this.connection = connection;

    executeUpdate(queryFactory.createTableCreationQuery());
  }

  @Override
  public void save(@NotNull String key, @NotNull String data) {
    String insertQuery = queryFactory.createJsonInsertionStatement();
    executeUpdate(insertQuery, key, data);
  }

  @Override
  public @Nullable String read(@NotNull String key) {
    String selectQuery = queryFactory.createValueSelectQuery();

    return executeQuery(selectQuery, resultSet -> {
      if (resultSet.next()) {
        return resultSet.getString(1);
      }

      throw new IllegalStateException("ResultSet is empty");
    }, key);
  }

  @Override
  public @NotNull Map<String, String> readAll() {
    String selectAllQuery = queryFactory.createSelectAllRowsQuery();

    return executeQuery(selectAllQuery, this::mapSetToMap);
  }

  @Override
  public @NotNull Map<String, String> readAllWithKey(@NotNull String key) {
    String selectAllQuery = queryFactory.createSelectAllRowsWithIdQuery();

    return executeQuery(selectAllQuery, this::mapSetToMap, key);
  }

  @Override
  public boolean delete(@NotNull String key) {
    String insertQuery = queryFactory.createDeletionStatement();
    return executeUpdate(insertQuery, key) != 0;
  }

  @Override
  public boolean isClosed() {
    try {
      return connection.isClosed();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }

  public @NotNull Connection connection() {
    return connection;
  }

  public @NotNull ParameterizedQueryFactory queryFactory() {
    return queryFactory;
  }

  private Map<String, String> mapSetToMap(ResultSet resultSet) throws SQLException {
    Map<String, String> map = new LinkedHashMap<>();
    while(resultSet.next()) {
      String key = resultSet.getString(1);
      String value = resultSet.getString(2);

      map.put(key, value);
    }
    return map;
  }

  private int executeUpdate(String query, Object... parameters) {
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      for (int i = 1; i <= parameters.length; i++) {
        statement.setObject(i, parameters[i - 1]);
      }
      return statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T executeQuery(String query, ResultSetMapper<T> mapper, Object... parameters) {
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      for (int i = 1; i <= parameters.length; i++) {
        statement.setObject(i, parameters[i - 1]);
      }
      return mapper.mapResultSet(statement.executeQuery());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  interface ResultSetMapper<T> {
    T mapResultSet(ResultSet resultSet) throws SQLException;
  }
}