package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.properties.APIConsumerProperties;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerBuilder;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumerImpl;
import com.github.ynverxe.blue.event.consumer.complex.handler.ConsumerHandler;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerProperties;
import com.github.ynverxe.blue.event.consumer.filter.ConsumerFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EventNodeImpl<T> extends ComplexConsumerImpl<T> implements EventNode<T> {

  private final @NotNull Class<T> baseEventType;
  private final Predicate<T> eventFilter;
  private final List<RegisteredConsumer> consumers = new CopyOnWriteArrayList<>();

  public EventNodeImpl(@NotNull Class<T> baseEventType, @NotNull Predicate<T> eventFilter) {
    this.baseEventType = baseEventType;
    this.eventFilter = eventFilter;
  }

  public EventNodeImpl(@NotNull Class<T> baseEventType) {
    this(baseEventType, event -> true);
  }

  @Override
  public void consume(@NotNull EventDispatcher<T> caller, @NotNull T event) {
    dispatchEvent(event);
  }

  @Override
  public @NotNull DispatchResult<T> dispatchEvent(@NotNull T event) {
    Class eventClass = event.getClass();

    if (!eventFilter.test(event)) return DispatchResult.nonDispatched(event);

    Stream<EventConsumer> consumers = consumersFor(eventClass);

    Map<EventConsumer<? extends T>, Throwable> errors = new LinkedHashMap<>();

    consumers.forEach(consumer -> {
      try {
        consumer.consume(this, event);
      } catch (Throwable throwable) {
        errors.put(consumer, throwable);
      }
    });

    return new DispatchResult<>(event, errors);
  }

  @Override
  public <E extends T> void addEventConsumer(
    @NotNull Class<E> eventType, @NotNull EventConsumer<E> consumer) {
    if (consumer == this) return;

    if (matchConsumer((registeredType, registeredConsumer) ->
      registeredType == eventType && consumer == registeredConsumer).isPresent()) return;

    if (consumer instanceof ExpirableConsumer) {
      if (((ExpirableConsumer<E>) consumer).expired()) return;

      ((ExpirableConsumer<E>) consumer).handleExpiration(this::removeEventConsumer);
    }

    this.consumers.add(new RegisteredConsumer(consumer, eventType));
  }

  @Override
  public void addGlobalConsumer(@NotNull EventConsumer<T> consumer) {
    ComplexConsumerBuilder<T, ?> builder = ComplexConsumer.newBuilder();
    builder.backing(consumer).makeAbstract();
    addEventConsumer(baseEventType, builder.build());
  }

  @Override
  public @NotNull List<EventConsumer<T>> removeEventConsumers(@NotNull ConsumerFilter<T> filter) {
    List<EventConsumer<T>> removed = new ArrayList<>();

    for (RegisteredConsumer registeredConsumer : this.consumers) {
      EventConsumer consumer = registeredConsumer.consumer;
      if (filter.check(registeredConsumer.eventType, consumer)) {
        removed.add(consumer);
        this.consumers.remove(registeredConsumer);
      }
    }

    return removed;
  }

  @Override
  public @Nullable <E extends T> EventConsumer<E> removeEventConsumer(@NotNull EventConsumer<E> eventConsumer) {
    List<EventConsumer<T>> removed = removeEventConsumers((type, consumer) -> consumer == eventConsumer);

    return removed.size() != 0 ? (EventConsumer<E>) removed.get(0) : null;
  }

  @Override
  public @NotNull <E extends T> Stream<EventConsumer<E>> consumersFor(@NotNull Class<E> eventType) {
    return consumers.stream()
      .filter(registeredConsumer -> {
        EventConsumer consumer = registeredConsumer.consumer;
        Class consumerEventType = registeredConsumer.eventType;

        if (APIConsumerProperties.ABSTRACT.isPresent(consumer)) {
          return consumerEventType.isAssignableFrom(eventType);
        }

        return consumerEventType.equals(eventType);
      })
      .map(registeredConsumer -> registeredConsumer.consumer);
  }

  @Override
  public @NotNull List<EventConsumer<T>> matchConsumers(@NotNull ConsumerFilter<T> filter) {
    List<EventConsumer<T>> matched = new ArrayList<>();

    for (RegisteredConsumer registeredConsumer : this.consumers) {
      if (filter.check(registeredConsumer.eventType, registeredConsumer.consumer)) {
        matched.add(registeredConsumer.consumer);
      }
    }

    return matched;
  }

  @Override
  public @NotNull Optional<EventConsumer<T>> matchConsumer(@NotNull ConsumerFilter<T> filter) {
    for (RegisteredConsumer registeredConsumer : this.consumers) {
      if (filter.check(registeredConsumer.eventType, registeredConsumer.consumer)) {
        return Optional.of(registeredConsumer.consumer);
      }
    }

    return Optional.empty();
  }

  @Override
  public @NotNull Map<Class<? extends T>, List<EventConsumer<? extends T>>> eventConsumers() {
    Map<Class<? extends T>, List<EventConsumer<? extends T>>> consumerMap = new LinkedHashMap<>();

    for (RegisteredConsumer consumer : this.consumers) {
      consumerMap.computeIfAbsent(consumer.eventType, (k) -> new ArrayList<>())
        .add(consumer.consumer);
    }

    return consumerMap;
  }

  @Override
  public void clearChildrenNodes() {
    removeEventConsumers((type, consumer) -> consumer instanceof EventNode);
  }

  @Override
  public @NotNull List<EventNode<?>> childrenNodes() {
    return consumers.stream()
      .filter(registeredConsumer -> registeredConsumer.consumer instanceof EventNode)
      .map(registeredConsumer -> (EventNode<?>) registeredConsumer.consumer)
      .collect(Collectors.toList());
  }

  @Override
  public boolean isAbstract() {
    return true;
  }

  @Override
  public @NotNull Class<T> baseEventType() {
    return baseEventType;
  }

  private static class RegisteredConsumer {
    private final EventConsumer consumer;
    private final Class eventType;

    private RegisteredConsumer(EventConsumer consumer, Class eventType) {
      this.consumer = consumer;
      this.eventType = eventType;
    }
  }
}