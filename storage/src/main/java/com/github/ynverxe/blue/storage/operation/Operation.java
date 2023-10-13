package com.github.ynverxe.storage4all.operation;

import com.github.ynverxe.storage4all.exception.ErrorHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Operation<T> {

  static @NotNull <T> Operation<List<T>> ofSyncOperations(
    @NotNull List<Operation<T>> operations, @Nullable Executor executor) {
    return new SimpleOperation<>(
      () -> {
        List<T> values = new ArrayList<>();
        for (Operation<T> operation : operations) {
          try {
            values.add(operation.run());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return values;
      },
      executor);
  }

  static <T> @NotNull Operation<T> of(
    @NotNull Callable<T> valueComputer, @Nullable Executor executor) {
    return new SimpleOperation<>(valueComputer, executor);
  }

  static <T> @NotNull Operation<T> of(@NotNull Callable<T> valueComputer) {
    return new SimpleOperation<>(valueComputer);
  }

  static @NotNull Operation<Void> empty() {
    return new SimpleOperation<>(() -> null, null);
  }

  T run();

  @NotNull
  CompletableFuture<T> runAsync(@NotNull Executor executor);

  @NotNull
  CompletableFuture<T> runAsync();

  @NotNull
  Operation<Void> and(@NotNull Operation<?> toAdd);

  @NotNull
  Operation<T> post(@NotNull Consumer<T> consumer);

  @NotNull <S> Operation<S> map(@NotNull Function<T, S> mapper);

  @Contract("_ -> this")
  @NotNull
  Operation<T> errorHandler(@NotNull ErrorHandler errorHandler);
}