package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public class DispatchResult<T> {

  private final boolean dispatched;
  private final @NotNull T event;
  private final @NotNull Map<EventConsumer<? extends T>, Throwable> errors;

  public DispatchResult(
    boolean dispatched, @NotNull T event, @NotNull Map<EventConsumer<? extends T>, Throwable> errors) {
    this.dispatched = dispatched;
    this.event = event;
    this.errors = errors;
  }

  public DispatchResult(@NotNull T event, @NotNull Map<EventConsumer<? extends T>, Throwable> errors) {
    this.dispatched = true;
    this.event = event;
    this.errors = errors;
  }

  public static <T> @NotNull DispatchResult<T> nonDispatched(@NotNull T event) {
    return new DispatchResult<>(false, event, Collections.emptyMap());
  }

  public boolean dispatched() {
    return dispatched;
  }

  public @NotNull T event() {
    return event;
  }

  public @NotNull Map<EventConsumer<? extends T>, Throwable> errors() {
    return errors;
  }
}