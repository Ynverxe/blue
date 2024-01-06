package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerBuilder;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumer;
import com.github.ynverxe.blue.event.consumer.filter.ConsumerFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface EventNode<T> extends EventDispatcher<T>, ComplexConsumer<T> {

  static <T> @NotNull EventNodeBuilder<T> newBuilder(@NotNull Class<T> baseEventType) {
    return new EventNodeBuilder<>(baseEventType);
  }

  static <T> @NotNull EventNode<T> create(@NotNull Class<T> baseEventType, @NotNull Predicate<T> eventFilter) {
    return newBuilder(baseEventType).eventFilter(eventFilter).build();
  }

  static <T> @NotNull EventNode<T> create(@NotNull Class<T> baseEventType) {
    return create(baseEventType, event -> true);
  }

  <E extends T> void addEventConsumer(
    @NotNull Class<E> eventType, @NotNull EventConsumer<E> consumer);

  default <E extends T> void addEventConsumer(
    @NotNull Class<E> eventType, @NotNull Consumer<ComplexConsumerBuilder<E, ?>> consumerConfigurator) {
    ComplexConsumerBuilder<E, ?> builder = ComplexConsumer.newBuilder();
    consumerConfigurator.accept(builder);
    addEventConsumer(eventType, builder.build());
  }

  void addGlobalConsumer(@NotNull EventConsumer<T> consumer);

  @NotNull List<EventConsumer<T>> removeEventConsumers(@NotNull ConsumerFilter<T> filter);

  <E extends T> @Nullable EventConsumer<E> removeEventConsumer(@NotNull EventConsumer<E> eventConsumer);

  <E extends T> @NotNull Stream<EventConsumer<E>> consumersFor(@NotNull Class<E> eventType);

  @NotNull List<EventConsumer<T>> matchConsumers(@NotNull ConsumerFilter<T> filter);

  @NotNull Optional<EventConsumer<T>> matchConsumer(@NotNull ConsumerFilter<T> filter);

  @NotNull Map<Class<? extends T>, List<EventConsumer<? extends T>>> eventConsumers();

  void clearChildrenNodes();

  @NotNull List<EventNode<?>> childrenNodes();

  @NotNull Class<T> baseEventType();
}