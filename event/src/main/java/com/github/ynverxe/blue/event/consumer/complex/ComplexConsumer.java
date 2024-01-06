package com.github.ynverxe.blue.event.consumer.complex;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerPropertiesView;
import org.jetbrains.annotations.NotNull;

public interface ComplexConsumer<E> extends EventConsumer<E> {
  @NotNull ConsumerPropertiesView propertiesView();

  static <E, B extends ComplexConsumerBuilder<E, B>> ComplexConsumerBuilder<E, B> newBuilder(
    @NotNull Class<E> eventType) {
    return new ComplexConsumerBuilder<>();
  }

  static <E, B extends ComplexConsumerBuilder<E, B>> ComplexConsumerBuilder<E, B> newBuilder() {
    return new ComplexConsumerBuilder<>();
  }
}