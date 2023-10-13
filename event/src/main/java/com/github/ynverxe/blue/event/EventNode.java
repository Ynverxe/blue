package com.github.ynverxe.blue.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface EventNode<T> extends EventDispatcher<T>, EventConsumer<T> {

  static <T> @NotNull EventNode<T> create(@NotNull Class<T> baseEventType, @NotNull Predicate<T> eventFilter) {
    return new EventNodeImpl<>(baseEventType, eventFilter);
  }

  static <T> @NotNull EventNode<T> create(@NotNull Class<T> baseEventType) {
    return create(baseEventType, event -> true);
  }

  <E extends T> void addEventConsumer(
    @NotNull Class<E> eventType, @NotNull EventConsumer<E> consumer);

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