package com.github.ynverxe.blue.storage;

import com.github.ynverxe.blue.storage.crud.CrudRepository;
import com.github.ynverxe.blue.storage.internal.repository.ModelRepositoryImpl;
import com.github.ynverxe.blue.storage.model.ModelSerializer;
import com.github.ynverxe.blue.storage.repository.ModelRepository;
import com.github.ynverxe.blue.storage.repository.SavableModel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ModelStorage<D, T extends SavableModel> {

  private @NotNull CrudRepository<D> crudRepository;
  private @NotNull ModelSerializer<D, T> modelSerializer;
  private @NotNull Executor executor;
  private @NotNull Consumer<Throwable> errorHandler;

  private final @NotNull ModelRepository<T> modelRepository;

  public ModelStorage(
    @NotNull CrudRepository<D> crudRepository,
    @NotNull ModelSerializer<D, T> modelSerializer,
    @NotNull Executor executor,
    @NotNull Consumer<Throwable> errorHandler
  ) {
    this.crudRepository = crudRepository;
    this.modelSerializer = modelSerializer;
    this.executor = executor;
    this.errorHandler = errorHandler;
    this.modelRepository = new ModelRepositoryImpl<>(this);
  }

  public ModelStorage(
    @NotNull CrudRepository<D> crudRepository,
    @NotNull ModelSerializer<D, T> modelSerializer,
    @NotNull Executor executor
  ) {
    this(crudRepository, modelSerializer, executor, throwable -> {
      if (throwable instanceof RuntimeException) {
        throw (RuntimeException) throwable;
      }

      throw new RuntimeException(throwable);
    });
  }

  public ModelStorage(@NotNull CrudRepository<D> crudRepository, @NotNull ModelSerializer<D, T> modelSerializer) {
    this(crudRepository, modelSerializer, Executors.newCachedThreadPool());
  }

  public CrudRepository<D> crudRepository() {
    return crudRepository;
  }

  public synchronized void setCrudRepository(@NotNull CrudRepository<D> crudRepository) {
    this.crudRepository = crudRepository;
  }

  public ModelSerializer<D, T> modelSerializer() {
    return modelSerializer;
  }

  public synchronized void setModelSerializer(@NotNull ModelSerializer<D, T> modelSerializer) {
    this.modelSerializer = modelSerializer;
  }

  public Executor executor() {
    return executor;
  }

  public synchronized void setExecutor(@NotNull Executor executor) {
    this.executor = executor;
  }

  public Consumer<Throwable> errorHandler() {
    return errorHandler;
  }

  public synchronized void setErrorHandler(@NotNull Consumer<Throwable> errorHandler) {
    this.errorHandler = errorHandler;
  }

  public ModelRepository<T> modelRepository() {
    return modelRepository;
  }
}