package com.github.ynverxe.blue.collection.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CacheImpl<V> extends AbstractCache.Mutable<V> {
  public CacheImpl(@NotNull Map<String, V> backing, @NotNull Class<V> valueType) {
    super(backing, valueType);
  }

  public @Nullable V addValue(@NotNull String key, @NotNull V value) {
    return this.backing.put(key, value);
  }
}