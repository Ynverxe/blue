package com.github.ynverxe.blue.collection.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DelegatedCache<V> implements Cache<V> {

  private final Cache<V> delegate;

  public DelegatedCache(Cache<V> delegate) {
    this.delegate = delegate;
  }

  @Override
  public @Nullable V find(@NotNull String key) {
    return delegate.find(key);
  }

  @Override
  public @NotNull Map<String, V> asMap() {
    return delegate.asMap();
  }

  @Override
  public @NotNull List<String> keys() {
    return delegate.keys();
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public @NotNull Class<?> valueType() {
    return delegate.valueType();
  }

  @Override
  public boolean hasKey(@NotNull String key) {
    return delegate.hasKey(key);
  }

  @NotNull
  @Override
  public Iterator<V> iterator() {
    return delegate.iterator();
  }
}