package com.github.ynverxe.storage.sql.repository.query;

import org.jetbrains.annotations.NotNull;

public interface ParameterizedQueryFactory {

  @NotNull String createTableCreationQuery();

  @NotNull String createJsonInsertionStatement();

  @NotNull String createDeletionStatement();

  @NotNull String createValueSelectQuery();

  @NotNull String createSelectAllRowsWithIdQuery();

  @NotNull String createSelectAllRowsQuery();

}