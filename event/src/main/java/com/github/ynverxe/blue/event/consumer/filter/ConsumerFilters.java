package com.github.ynverxe.blue.event.consumer.filter;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumer;
import com.github.ynverxe.blue.event.consumer.complex.properties.ConsumerPropertiesView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class ConsumerFilters {

  private ConsumerFilters() {}

  public static <E> ConsumerFilter<E> of(@NotNull Class<E> eventClass, @NotNull Predicate<EventConsumer<E>> filter) {
    return (type, consumer) -> type.equals(eventClass) && filter.test(consumer);
  }

  public static <E> ConsumerFilter<E> of(@NotNull Predicate<EventConsumer<E>> filter) {
    return (type, consumer) -> filter.test(consumer);
  }

  public static <E> ConsumerFilter<E> complex(@NotNull Class<E> eventClass, @NotNull Predicate<ComplexConsumer<E>> filter) {
    return of(eventClass, consumer -> runComplexConsumerFilter(consumer, filter));
  }

  public static <E> ConsumerFilter<E> complex(@NotNull Predicate<ComplexConsumer<E>> filter) {
    return of(consumer -> runComplexConsumerFilter(consumer, filter));
  }

  public static <E> ConsumerFilter<E> properties(@NotNull Class<E> eventClass, @NotNull Predicate<ConsumerPropertiesView> filter) {
    return complex(eventClass, consumer -> runPropertiesFilter(consumer, filter));
  }

  public static <E> ConsumerFilter<E> properties(@NotNull Predicate<ConsumerPropertiesView> filter) {
    return complex(consumer -> runPropertiesFilter(consumer, filter));
  }

  private static boolean runPropertiesFilter(ComplexConsumer<?> consumer, Predicate<ConsumerPropertiesView> filter) {
    return filter.test(consumer.propertiesView());
  }

  private static <E> boolean runComplexConsumerFilter(EventConsumer<E> consumer, Predicate<ComplexConsumer<E>> filter) {
    if (consumer instanceof ComplexConsumer) {
      return filter.test((ComplexConsumer<E>) consumer);
    }

    return false;
  }
}