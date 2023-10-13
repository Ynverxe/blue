package com.github.ynverxe.storage4all;

import com.github.ynverxe.storage4all.operation.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Storage<T> {

  @NotNull
  Operation<T> loadModel(@NotNull String key);

  @NotNull
  Operation<Void> saveModel(@NotNull String key, @NotNull T model);

  default @NotNull <M extends T> Operation<List<String>> saveModels(
    @NotNull Collection<M> models, @NotNull Function<M, String> keyProvider) {
    List<Operation<String>> operations = new ArrayList<>();

    for (M model : models) {
      String key = keyProvider.apply(model);

      if (key == null) throw new IllegalArgumentException("null key");

      operations.add(saveModel(key, model).map(unused -> key));
    }

    return Operation.ofSyncOperations(operations, null);
  }

  @NotNull
  Operation<Boolean> deleteModelData(@NotNull String key);

  @NotNull
  Operation<List<String>> keys();

  @NotNull
  Operation<Map<String, T>> all();
}
