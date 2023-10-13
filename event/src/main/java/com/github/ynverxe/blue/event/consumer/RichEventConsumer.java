package com.github.ynverxe.blue.event.consumer;

import com.github.ynverxe.blue.event.EventConsumer;
import com.github.ynverxe.blue.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RichEventConsumer<T> extends ExpirableConsumer.Impl<T> implements EventConsumer<T> {

  private final boolean abstractHandling;
  private EventConsumer<T> action;
  private Set<Object> identifiers;
  private List<Handler<T>> handlers = new ArrayList<>();

  public RichEventConsumer(EventConsumer<T> action, Set<Object> identifiers, boolean abstractHandling) {
    this.action = action;
    this.abstractHandling = abstractHandling;
    if (identifiers.isEmpty()) identifiers = Collections.emptySet();
    this.identifiers = identifiers;
  }

  public static @NotNull <T> RichEventConsumerBuilder<T> builder() {
    return new RichEventConsumerBuilder<>();
  }

  public static @NotNull <T> RichEventConsumerBuilder<T> builder(@NotNull Class<T> eventType) {
    return new RichEventConsumerBuilder<>();
  }

  @Override
  public void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) throws Throwable {
    if (expired()) return;

    action.consume(caller, event);

    handlers.forEach(handler -> handler.postConsuming(caller, event, this));
  }

  @Override
  public @NotNull Set<Object> identifiers() {
    return identifiers;
  }

  public void addHandler(@NotNull Handler<T> handler) {
    this.handlers.add(handler);
  }

  @Override
  public void expire() {
    if (expired()) return;

    this.expirationAction.accept(this);
    this.expirationAction = null;
    this.action = null;
    this.handlers = null;
    this.identifiers = Collections.emptySet();
  }

  @Override
  public boolean isAbstract() {
    return abstractHandling;
  }

  public interface Handler<T> {
    void postConsuming(@NotNull EventDispatcher<T> caller, @NotNull T event, @NotNull RichEventConsumer<T> holder);
  }
}