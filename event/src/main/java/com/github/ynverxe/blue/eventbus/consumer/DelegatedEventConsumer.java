package com.github.ynverxe.blue.eventbus.consumer;

import com.github.ynverxe.blue.eventbus.EventConsumer;
import com.github.ynverxe.blue.eventbus.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class DelegatedEventConsumer<T> implements EventConsumer<T> {
  private final EventConsumer<T> delegate;
  private final boolean abstractHandling;

  public DelegatedEventConsumer(EventConsumer<T> delegate, boolean abstractHandling) {
    this.delegate = delegate;
    this.abstractHandling = abstractHandling;
  }

  public DelegatedEventConsumer(EventConsumer<T> delegate) {
    this(delegate, delegate.isAbstract());
  }

  @Override
  public void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) throws Throwable {
    delegate.consume(caller, event);
  }

  @Override
  public boolean isAbstract() {
    return abstractHandling;
  }
}