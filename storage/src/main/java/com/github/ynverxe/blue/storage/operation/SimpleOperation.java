package com.github.ynverxe.storage4all.operation;

import com.github.ynverxe.storage4all.exception.ErrorHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleOperation<T> implements Operation<T> {

  private final Callable<T> valueComputer;
  private final Executor executor;
  private ErrorHandler errorHandler = ErrorHandler.throwIt();

  SimpleOperation(Callable<T> valueComputer) {
    this(valueComputer, null);
  }

  SimpleOperation(Callable<T> valueComputer, Executor executor) {
    this.valueComputer = valueComputer;
    this.executor = executor != null ? executor : Runnable::run;
  }

  @Override
  public T run() {
    try {
      return valueComputer.call();
    } catch (Exception e) {
      try {
        return (T) errorHandler.provide(e);
      } catch (Throwable ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public @NotNull CompletableFuture<T> runAsync(@NotNull Executor executor) {
    return CompletableFuture.supplyAsync(this::run, executor)
      .exceptionally(
        throwable -> {
          throwable.printStackTrace();
          return null;
        });
  }

  @Override
  public @NotNull CompletableFuture<T> runAsync() {
    return runAsync(executor);
  }

  @Override
  public @NotNull Operation<Void> and(@NotNull Operation<?> toAdd) {
    return new SimpleOperation<>(
      () -> {
        SimpleOperation.this.valueComputer.call();
        toAdd.run();
        return null;
      },
      executor);
  }

  @Override
  public @NotNull Operation<T> post(@NotNull Consumer<T> consumer) {
    return new SimpleOperation<>(
      () -> {
        T value = SimpleOperation.this.valueComputer.call();
        consumer.accept(value);
        return value;
      },
      executor);
  }

  @Override
  public @NotNull <S> Operation<S> map(@NotNull Function<T, S> mapper) {
    return new SimpleOperation<>(
      () -> {
        T value = SimpleOperation.this.valueComputer.call();

        return mapper.apply(value);
      },
      executor);
  }

  @Override
  public @NotNull Operation<T> errorHandler(@NotNull ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }
}
