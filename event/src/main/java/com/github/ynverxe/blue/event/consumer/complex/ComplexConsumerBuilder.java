package com.github.ynverxe.blue.event.consumer.complex;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerProperties;
import com.github.ynverxe.blue.event.consumer.complex.handler.ConsumerHandler;
import com.github.ynverxe.blue.event.consumer.complex.handler.ExpireOnExecutionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.github.ynverxe.blue.event.consumer.complex.properties.APIConsumerProperties.*;

@SuppressWarnings("unchecked")
public class ComplexConsumerBuilder<E, B extends ComplexConsumerBuilder<E, B>> {

  protected final @NotNull List<ConsumerHandler<E>> handlers = new ArrayList<>();
  protected final @NotNull ConsumerProperties properties = new ConsumerProperties(new HashMap<>());
  protected @Nullable EventConsumer<E> backing;

  public @NotNull B backing(EventConsumer<E> backing) {
    this.backing = backing;
    return castThis();
  }

  @SafeVarargs
  public final @NotNull B handlers(@NotNull ConsumerHandler<E>... handlers) {
    this.handlers.addAll(Arrays.asList(handlers));
    return castThis();
  }

  public @NotNull B configureProperties(@NotNull Consumer<ConsumerProperties> configurator) {
    configurator.accept(properties);
    return castThis();
  }

  public @NotNull B makeAbstract() {
    return configureProperties(ABSTRACT::set);
  }

  public @NotNull B makeDisposable() {
    return configureProperties(DISPOSABLE::set)
      .handlers(new ExpireOnExecutionHandler<>());
  }

  public @NotNull ComplexConsumer<E> build() {
    if (backing == null)
      throw new IllegalStateException("No backing consumer provided");

    return new ComplexConsumerImpl<>(handlers, backing, properties);
  }

  protected B castThis() {
    return (B) this;
  }
}