package com.github.ynverxe.bus.eventbus;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface EventConsumer<T> {

  static <T> @NotNull EventConsumer<T> newAbstract(@NotNull Consumer<T> consumer) {
    return new EventConsumer<T>() {
      @Override
      public void consume(@NotNull T event) throws Throwable {
        consumer.accept(event);
      }

      @Override
      public boolean isAbstract() {
        return true;
      }
    };
  }

  void consume(@NotNull T event) throws Throwable;

  default boolean isAbstract() {
    return false;
  }
}