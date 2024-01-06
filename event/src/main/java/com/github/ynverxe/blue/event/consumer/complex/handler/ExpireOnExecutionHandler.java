package com.github.ynverxe.blue.event.consumer.complex.handler;

import com.github.ynverxe.blue.event.EventDispatcher;
import com.github.ynverxe.blue.event.EventNode;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerImpl;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerProperties;
import org.jetbrains.annotations.NotNull;

public class ExpireOnExecutionHandler<E> implements ConsumerHandler<E> {

  @Override
  public void postConsume(@NotNull ComplexConsumerImpl<E> caller, @NotNull ConsumerProperties context, @NotNull EventDispatcher<E> dispatcher, @NotNull E event) {
    if (dispatcher instanceof EventNode) {
      ((EventNode<E>) dispatcher).removeEventConsumer(caller);
    }
  }
}