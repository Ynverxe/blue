package com.github.ynverxe.storage4all.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ErrorHandler {

  static @NotNull ErrorHandler print() {
    return error -> {
      error.printStackTrace();
      return null;
    };
  }

  static @NotNull ErrorHandler throwIt() {
    return error -> {
      throw error;
    };
  }

  static <E extends Throwable> @NotNull ErrorHandler handle(
    @NotNull Class<E> exceptionType, Function<E, Object> function) {
    return error -> {
      if (!exceptionType.isInstance(error)) return null;

      return function.apply((E) error);
    };
  }

  static @NotNull ErrorHandler justConsume(Consumer<Throwable> throwableConsumer) {
    return error -> {
      throwableConsumer.accept(error);
      return null;
    };
  }

  @Nullable
  Object provide(@NotNull Throwable error) throws Throwable;

  default @NotNull ErrorHandler preHandling(@NotNull Consumer<Throwable> throwableConsumer) {
    return error -> {
      throwableConsumer.accept(error);
      return provide(error);
    };
  }

  default <E extends Throwable> @NotNull ErrorHandler handleSpecificError(
    @NotNull Class<E> exceptionType, Function<E, Object> function) {
    return error -> {
      if (!exceptionType.isInstance(error)) return null;

      return function.apply((E) error);
    };
  }
}
