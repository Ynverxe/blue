package com.github.ynverxe.blue.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;

public interface EventConsumer<T> {

  static <T> @NotNull EventConsumer<T> newAbstract(@NotNull BiConsumer<EventDispatcher<T>, T> consumer) {
    return new EventConsumer<T>() {
      @Override
      public void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) throws Throwable {
        consumer.accept(caller, event);
      }

      @Override
      public boolean isAbstract() {
        return true;
      }
    };
  }

  void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) throws Throwable;

  default @NotNull EventConsumer<T> and(@NotNull EventConsumer<T> consumer) {
    return (caller, event) -> {
      consume(caller, event);
      consumer.consume(caller, event);
    };
  }

  default @NotNull Set<Object> identifiers() {
    return Collections.emptySet();
  }

  default boolean hasIdentifier(@Nullable Object identifier) {
    return identifiers().contains(identifier);
  }

  default boolean isAbstract() {
    return false;
  }
}