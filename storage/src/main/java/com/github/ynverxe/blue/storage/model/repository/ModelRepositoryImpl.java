package com.github.ynverxe.blue.storage.model.repository;

import com.github.ynverxe.blue.storage.ModelStorage;
import com.github.ynverxe.blue.storage.crud.repository.CrudRepository;
import com.github.ynverxe.blue.storage.model.ModelSerializer;
import com.github.ynverxe.blue.storage.model.repository.ModelRepository;
import com.github.ynverxe.blue.storage.model.repository.SavableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.*;

public class ModelRepositoryImpl<D, T extends SavableModel> implements ModelRepository<T> {

  private final @NotNull ModelStorage<D, T> holder;

  public ModelRepositoryImpl(@NotNull ModelStorage<D, T> holder) {
    this.holder = holder;
  }

  @Override
  public void saveModel(@NotNull T model) {
    try {
      D serialized = serializer().serializeModel(model);
      crudRepository().save(model.id(), serialized);
    } catch (Throwable e) {
      errorHandler().accept(e);
    }
  }

  @Override
  public @Nullable T findModel(@NotNull String id) {
    try {
      D foundData = crudRepository().read(id);

      if (foundData == null) {
        return null;
      }

      return serializer().deserializeModel(foundData);
    } catch (Throwable e) {
      errorHandler().accept(e);
      return null;
    }
  }

  @Override
  public @NotNull List<T> findAllWithId(@NotNull String id) {
    return readMultipleModels(repository -> repository.readAllWithKey(id));
  }

  @Override
  public @NotNull List<T> findAll() {
    return readMultipleModels(CrudRepository::readAll);
  }

  private List<T> readMultipleModels(Function<CrudRepository<D>, Map<String, D>> function) {
    try {
      Map<String, D> foundData = function.apply(crudRepository());

      if (foundData.isEmpty()) {
        return Collections.emptyList();
      }

      List<T> models = new ArrayList<>();
      for (D value : foundData.values()) {
        models.add(serializer().deserializeModel(value));
      }

      return models;
    } catch (Throwable e) {
      errorHandler().accept(e);
      return Collections.emptyList();
    }
  }

  @Override
  public boolean deleteModel(@NotNull String id) {
    try {
      return crudRepository().delete(id);
    } catch (Throwable e) {
      errorHandler().accept(e);
      return false;
    }
  }

  @Override
  public @NotNull CompletableFuture<Boolean> deleteModelAsync(@NotNull String id) {
    return supplyAsync(() -> deleteModel(id), executor());
  }

  @Override
  public @NotNull CompletableFuture<Void> saveModelAsync(@NotNull T model) {
    return runAsync(() -> saveModel(model), executor());
  }

  @Override
  public @NotNull CompletableFuture<T> findModelAsync(@NotNull String id) {
    return supplyAsync(() -> findModel(id), executor());
  }

  @Override
  public @NotNull CompletableFuture<List<T>> findAllWithIdAsync(@NotNull String id) {
    return supplyAsync(() -> findAllWithId(id), executor());
  }

  @Override
  public @NotNull CompletableFuture<List<T>> findAllAsync() {
    return supplyAsync(this::findAll, executor());
  }

  private CrudRepository<D> crudRepository() {
    return holder.crudRepository();
  }

  private Executor executor() {
    return holder.executor();
  }

  private ModelSerializer<D, T> serializer() {
    return holder.modelSerializer();
  }

  private Consumer<Throwable> errorHandler() {
    return holder.errorHandler();
  }
}