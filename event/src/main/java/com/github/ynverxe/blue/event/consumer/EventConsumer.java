package com.github.ynverxe.blue.event.consumer;

import com.github.ynverxe.blue.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public interface EventConsumer<T> {

  void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) throws Throwable;

  static <T> EventConsumer<T> empty() {
    return (caller, event) -> {};
  }
}