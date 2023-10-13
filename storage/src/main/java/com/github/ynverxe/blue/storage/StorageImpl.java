package com.github.ynverxe.blue.storage;

import com.github.ynverxe.blue.storage.operation.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageImpl<T, D> implements Storage<T> {

  private final @NotNull DatabaseHandler<D> databaseHandler;
  private final @NotNull ModelSerializer<T, D> modelSerializer;

  public StorageImpl(
    @NotNull DatabaseHandler<D> databaseHandler, @NotNull ModelSerializer<T, D> modelSerializer) {
    this.databaseHandler = databaseHandler;
    this.modelSerializer = modelSerializer;
  }

  @Override
  public @NotNull Operation<T> loadModel(@NotNull String key) {
    return Operation.of(
      () -> {
        D dataFound = databaseHandler.findData(key);

        if (dataFound == null) return null;

        return modelSerializer.deserialize(key, dataFound);
      });
  }

  @Override
  public @NotNull Operation<Void> saveModel(@NotNull String key, @NotNull T model) {
    return Operation.of(
      () -> {
        D serialized = modelSerializer.serialize(key, model);

        databaseHandler.saveData(key, serialized);
        return null;
      });
  }

  @Override
  public @NotNull Operation<Boolean> deleteModelData(@NotNull String key) {
    return Operation.of(() -> databaseHandler.deleteData(key));
  }

  @Override
  public @NotNull Operation<List<String>> keys() {
    return Operation.of(databaseHandler::collectKeys);
  }

  @Override
  public @NotNull Operation<Map<String, T>> all() {
    return Operation.of(
      () ->
        databaseHandler.collectAll().entrySet().stream()
          .map(
            entry -> {
              try {
                return new AbstractMap.SimpleEntry<>(
                  entry.getKey(), modelSerializer.deserialize(entry.getKey(), entry.getValue()));
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }
}
