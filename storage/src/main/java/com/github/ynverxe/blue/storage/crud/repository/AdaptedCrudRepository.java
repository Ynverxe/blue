package com.github.ynverxe.blue.storage.crud.repository;

import com.github.ynverxe.blue.storage.crud.repository.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdaptedCrudRepository<I, O> implements CrudRepository<O> {

  private final @NotNull CrudRepository<I> backing;
  private final @NotNull RawDataAdapter<I, O> adapter;

  public AdaptedCrudRepository(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, O> adapter) {
    this.backing = backing;
    this.adapter = adapter;
  }

  @Override
  public void save(@NotNull String key, @NotNull O data) {
    backing.save(key, adapter.adaptToBackingData(data));
  }

  @Override
  public @Nullable O read(@NotNull String key) {
    I found = backing.read(key);

    if (found == null) return null;

    return adapter.adaptToNewData(found);
  }

  @Override
  public @NotNull Map<String, O> readAll() {
    Map<String, I> data = backing.readAll();
    return adaptMap(data);
  }

  @Override
  public @NotNull Map<String, O> readAllWithKey(@NotNull String key) {
    Map<String, I> data = backing.readAllWithKey(key);
    return adaptMap(data);
  }

  @Override
  public boolean delete(@NotNull String key) {
    return backing.delete(key);
  }

  public CrudRepository<I> backing() {
    return backing;
  }

  public RawDataAdapter<I, O> adapter() {
    return adapter;
  }

  private Map<String, O> adaptMap(Map<String, I> data) {
    Map<String, O> adapted = new LinkedHashMap<>();

    data.forEach((key, value) -> adapted.put(key, adapter.adaptToNewData(value)));

    return adapted;
  }

  @Override
  public boolean isClosed() {
    return backing.isClosed();
  }

  @Override
  public void close() throws IOException {
    backing.close();
  }
}