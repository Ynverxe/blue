package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EventNodeBuilder<E> extends ComplexConsumerBuilder<E, EventNodeBuilder<E>> {

  private final @NotNull Class<E> baseEventType;
  private @NotNull Predicate<E> eventFilter = e -> true;

  public EventNodeBuilder(@NotNull Class<E> baseEventType) {
    this.baseEventType = baseEventType;
  }

  @Override
  public @NotNull EventNodeBuilder<E> backing(EventConsumer<E> backing) {
    throw new UnsupportedOperationException("EventNodes doesn't uses a backing consumer");
  }

  public @NotNull EventNodeBuilder<E> eventFilter(Predicate<E> eventFilter) {
    this.eventFilter = eventFilter;
    return this;
  }

  @Override
  public @NotNull EventNode<E> build() {
    return new EventNodeImpl<>(handlers, properties, baseEventType, eventFilter);
  }
}