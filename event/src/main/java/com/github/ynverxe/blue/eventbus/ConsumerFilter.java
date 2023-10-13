package com.github.ynverxe.bus.eventbus;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ConsumerFilter<T> {

    boolean check(@NotNull Class<? extends T> type, @NotNull EventConsumer<T> consumer);

}