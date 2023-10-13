package com.github.ynverxe.blue.storage;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface ModelSerializer<T, D> {

  static @NotNull <T, D> ModelSerializer<T, D> get(
    @NotNull BiFunction<String, T, D> serializer, @NotNull BiFunction<String, D, T> deserializer) {
    return new ModelSerializer<T, D>() {
      @Override
      public @NotNull D serialize(@NotNull String modelStorageKey, T model) throws Exception {
        return serializer.apply(modelStorageKey, model);
      }

      @Override
      public @NotNull T deserialize(@NotNull String modelStorageKey, @NotNull D data) throws Exception {
        return deserializer.apply(modelStorageKey, data);
      }
    };
  }

  @NotNull
  D serialize(@NotNull String modelStorageKey, @NotNull T model) throws Exception;

  @NotNull
  T deserialize(@NotNull String modelStorageKey, @NotNull D data) throws Exception;
}
