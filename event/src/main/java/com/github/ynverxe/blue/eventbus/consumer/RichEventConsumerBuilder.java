package com.github.ynverxe.blue.eventbus.consumer;

import com.github.ynverxe.blue.eventbus.EventConsumer;
import com.github.ynverxe.blue.eventbus.consumer.handler.ExpireOnExecutionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RichEventConsumerBuilder<T> {

  private final Set<Object> identifiers = new HashSet<>();
  private final List<RichEventConsumer.Handler<T>> handlers = new ArrayList<>();
  private boolean disposable;
  private boolean abstractHandling;
  private EventConsumer<T> consumer = (caller, event) -> {
  };

  public RichEventConsumerBuilder<T> abstractHandling(boolean abstractHandling) {
    this.abstractHandling = abstractHandling;
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> identifiers(@NotNull Object... identifiers) {
    this.identifiers.addAll(Arrays.asList(identifiers));
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> identifiers(@NotNull Collection<Object> identifiers) {
    this.identifiers.addAll(identifiers);
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> andAction(@NotNull EventConsumer<T> action) {
    this.consumer = consumer.and(action);
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> consumer(@NotNull EventConsumer<T> consumer) {
    this.consumer = consumer;
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> disposable(boolean disposable) {
    this.disposable = disposable;
    return this;
  }

  public @NotNull RichEventConsumerBuilder<T> handler(@NotNull RichEventConsumer.Handler<T> handler) {
    this.handlers.add(handler);
    return this;
  }

  public @NotNull RichEventConsumer<T> build() {
    RichEventConsumer<T> consumer = new RichEventConsumer<>(this.consumer, identifiers, abstractHandling);

    if (disposable) {
      consumer.addHandler(new ExpireOnExecutionHandler());
    }

    handlers.forEach(consumer::addHandler);

    return consumer;
  }
}