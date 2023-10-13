package com.github.ynverxe.blue.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DatabaseHandler<T> extends Closeable {

  void saveData(@NotNull String key, @NotNull T data) throws Exception;

  boolean deleteData(@NotNull String key) throws Exception;

  boolean exists(@NotNull String key);

  @Nullable
  T findData(@NotNull String key) throws Exception;

  @NotNull
  List<String> collectKeys() throws Exception;

  default @NotNull Map<String, T> collectAll() throws Exception {
    Map<String, T> dataValues = new LinkedHashMap<>();

    for (String key : collectKeys()) {
      dataValues.put(key, findData(key));
    }

    return dataValues;
  }

  default @NotNull <O> DatabaseHandler<O> adapt(@NotNull DataAdapter<T, O> dataAdapter) {
    return new AdaptedDirectoryDatabaseHandler<>(this, dataAdapter);
  }
}
