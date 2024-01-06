package com.github.ynverxe.blue.event.consumer.filter;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ConsumerFilter<T> {

  boolean check(@NotNull Class<? extends T> type, @NotNull EventConsumer<T> consumer);

}