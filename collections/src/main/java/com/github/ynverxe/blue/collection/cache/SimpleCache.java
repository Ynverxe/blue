package com.github.ynverxe.blue.collection.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleCache<V> implements Cache<V> {

  protected final @NotNull Map<String, V> backing;
  private final @NotNull Class<V> valueType;

  public SimpleCache(@NotNull Map<String, V> backing, @NotNull Class<V> valueType) {
    this.backing = backing;
    this.valueType = valueType;
  }

  @Override
  public @Nullable V find(@NotNull String key) {
    return backing.get(key);
  }

  @Override
  public @NotNull Map<String, V> asMap() {
    return new LinkedHashMap<>(backing);
  }

  @Override
  public @NotNull List<String> keys() {
    return new ArrayList<>(backing.keySet());
  }

  @Override
  public int size() {
    return backing.size();
  }

  @Override
  public @NotNull Class<?> valueType() {
    return valueType;
  }

  @Override
  public boolean hasKey(@NotNull String key) {
    return backing.containsKey(key);
  }

  @NotNull
  @Override
  public Iterator<V> iterator() {
    return Collections.unmodifiableCollection(backing.values()).iterator();
  }

  static class Mutable<V> extends SimpleCache<V> implements Cache.Mutable<V> {

    public Mutable(@NotNull Map<String, V> backing, @NotNull Class<V> valueType) {
      super(backing, valueType);
    }

    @Override
    public V removeValue(@NotNull String key) {
      return backing.remove(key);
    }

    @Override
    public void clear() {
      backing.clear();
    }
  }
}
