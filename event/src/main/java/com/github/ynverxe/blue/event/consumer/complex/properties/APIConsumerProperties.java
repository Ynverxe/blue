package com.github.ynverxe.blue.event.consumer.complex.properties;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerImpl;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerProperties;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerPropertiesView;
import org.jetbrains.annotations.NotNull;

public enum APIConsumerProperties {
  /**
   * This property means that a consumer can
   * consume more than one event type. Normally,
   * events that extends from the event class that is
   * declared for that consumer.
   */
  ABSTRACT,
  /**
   * This property means that a consumer
   * expires when is called.
   */
  DISPOSABLE;

  public boolean isPresent(@NotNull EventConsumer<?> consumer) {
    return consumer instanceof ComplexConsumerImpl && isPresent(((ComplexConsumerImpl<?>) consumer).propertiesView());
  }

  public boolean isPresent(@NotNull ConsumerPropertiesView properties) {
    return properties.getProperty(name()) == this;
  }

  public void set(@NotNull ConsumerProperties properties) {
    properties.setProperty(name(), this);
  }

  public void remove(@NotNull ConsumerProperties properties) {
    properties.setProperty(name(), null);
  }
}