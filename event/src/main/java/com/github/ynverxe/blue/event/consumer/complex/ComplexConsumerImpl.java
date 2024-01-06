package com.github.ynverxe.blue.event.consumer.complex;

import com.github.ynverxe.blue.event.EventDispatcher;
import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerProperties;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerPropertiesView;
import com.github.ynverxe.blue.event.consumer.complex.handler.ConsumerHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ComplexConsumerImpl<E> implements ComplexConsumer<E> {

  private final @NotNull List<ConsumerHandler<E>> handlers;
  private final @NotNull EventConsumer<E> backingConsumer;
  private final ConsumerProperties properties;

  protected ComplexConsumerImpl(
    @NotNull List<ConsumerHandler<E>> handlers,
    @NotNull EventConsumer<E> backingConsumer,
    @NotNull ConsumerProperties properties
  ) {
    this.handlers = handlers;
    this.backingConsumer = backingConsumer;
    this.properties = properties;
  }

  @Override
  public void consume(@NotNull EventDispatcher<E> caller, @NotNull E event) throws Throwable {
    backingConsumer.consume(caller, event);

    for (ConsumerHandler<E> handler : handlers) {
      handler.postConsume(this, properties, caller, event);
    }
  }

  @Override
  public @NotNull ConsumerPropertiesView propertiesView() {
    return properties.newView();
  }
}