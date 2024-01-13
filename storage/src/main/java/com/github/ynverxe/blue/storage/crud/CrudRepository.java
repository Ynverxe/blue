package com.github.ynverxe.blue.storage.crud;

import org.jetbrains.annotations.NotNull;

/**
 * The data type that this executor uses.
 */
public interface CRUDExecutor<T> {

  void save(@NotNull String key, @NotNull T data);

  @NotNull T read(@NotNull String key);

  
}