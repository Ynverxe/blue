package com.github.ynverxe.xenov.common.cache;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public interface Cache<V> extends Iterable<V>, Examinable {

  @Nullable
  V find(@NotNull String key);

  @Nullable
  default Map.Entry<String, V> atIndex(int index) {
    if (index >= size()) return null;

    Iterator<Map.Entry<String, V>> iterator = asMap().entrySet().iterator();

    int current = 0;
    while (iterator.hasNext()) {
      Map.Entry<String, V> entry = iterator.next();
      if (current++ == index) {
        return entry;
      }
    }

    return null;
  }

  @Nullable
  default V valueAtIndex(int index) {
    Map.Entry<String, V> entry = atIndex(index);

    return entry != null ? entry.getValue() : null;
  }

  @Nullable
  default String keyAtIndex(int index) {
    Map.Entry<String, V> entry = atIndex(index);

    return entry != null ? entry.getKey() : null;
  }

  default @NotNull Optional<V> safeFind(@NotNull String key) {
    return Optional.ofNullable(find(key));
  }

  @NotNull
  Map<String, V> asMap();

  @NotNull List<String> keys();

  int size();

  @NotNull
  Class<?> valueType();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
            ExaminableProperty.of("size", size()),
            ExaminableProperty.of("valueType", valueTypeName()),
            ExaminableProperty.of("content", asMap()));
  }

  default String valueTypeName() {
    return valueType().getSimpleName();
  }

  default @Nullable String matchKey(@NotNull V value) {
    int index = valueIndex(value);
    if (index == -1) return null;

    return keys().get(index);
  }

  default int valueIndex(@Nullable V value) {
    Map<String, V> map = asMap();

    int i = 0;
    for (V v : map.values()) {
      if (v.equals(value)) {
        return i;
      }
    }

    return -1;
  }

  boolean hasKey(@NotNull String key);

  static @NotNull <V> Cache<V> create(@NotNull Class<V> valueType, @NotNull Map<String, V> map) {
    return new SimpleCache<>(map, valueType);
  }

  static @NotNull <V> Cache<V> concurrent(@NotNull Class<V> valueType) {
    return create(valueType, new ConcurrentHashMap<>());
  }

  interface Mutable<V> extends Cache<V> {
    V removeValue(@NotNull String key);

    void clear();
  }
}
