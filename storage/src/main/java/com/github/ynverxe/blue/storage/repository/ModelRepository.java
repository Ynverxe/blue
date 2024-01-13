package com.github.ynverxe.blue.storage.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ModelRepository<T extends SavableModel> {

  void saveModel(@NotNull T model);

  @Nullable T findModel(@NotNull String id);

  @NotNull List<T> findAllWithId(@NotNull String id);

  @NotNull List<T> findAll();

  boolean deleteModel(@NotNull String id);

  @NotNull CompletableFuture<Boolean> deleteModelAsync(@NotNull String id);

  @NotNull CompletableFuture<Void> saveModelAsync(@NotNull T model);

  @NotNull CompletableFuture<T> findModelAsync(@NotNull String id);

  @NotNull CompletableFuture<List<T>> findAllWithIdAsync(@NotNull String id);

  @NotNull CompletableFuture<List<T>> findAllAsync();

}