package com.github.ynverxe.blue.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class AdaptedDatabaseHandler<I, O> implements DatabaseHandler<O> {

  private final DatabaseHandler<I> backing;
  private final DataAdapter<I, O> dataAdapter;

  AdaptedDatabaseHandler(DatabaseHandler<I> backing, DataAdapter<I, O> dataAdapter) {
    this.backing = backing;
    this.dataAdapter = dataAdapter;
  }

  @Override
  public void saveData(@NotNull String key, @NotNull O data) throws Exception {
    backing.saveData(key, dataAdapter.adaptFromOutput(data));
  }

  @Override
  public boolean deleteData(@NotNull String key) throws Exception {
    return backing.deleteData(key);
  }

  @Override
  public boolean exists(@NotNull String key) {
    return backing.exists(key);
  }

  @Override
  public @Nullable O findData(@NotNull String key) throws Exception {
    I found = backing.findData(key);

    return found != null ? dataAdapter.adaptFromInput(found) : null;
  }

  @Override
  public @NotNull List<String> collectKeys() throws Exception {
    return backing.collectKeys();
  }

  @Override
  public void close() throws IOException {
    backing.close();
  }
}
