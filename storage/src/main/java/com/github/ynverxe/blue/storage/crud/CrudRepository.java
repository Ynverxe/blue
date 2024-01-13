package com.github.ynverxe.blue.storage.crud;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A model to execute generic database operations.
 *
 * @param <T> The data type that this repository uses.
 */
public interface CrudRepository<T> {

  void save(@NotNull String key, @NotNull T data);

  @Nullable T read(@NotNull String key);

  @NotNull Map<String, T> readAll();

  @NotNull Map<String, T> readAllWithKey(@NotNull String key);

  boolean delete(@NotNull String key);

}