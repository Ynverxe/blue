package com.github.ynverxe.blue.eventbus;

import com.github.ynverxe.blue.eventbus.consumer.DisposableEventConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListenerBuilder<T> {

    private final Set<Object> identifiers = new LinkedHashSet<>();

    private boolean disposable;
    private EventConsumer<T> consumer = (caller, event) -> {};

    public @NotNull ListenerBuilder<T> identifiers(@NotNull Object... identifiers) {
        this.identifiers.addAll(Arrays.asList(identifiers));
        return this;
    }

    public @NotNull ListenerBuilder<T> identifiers(@NotNull Collection<Object> identifiers) {
        this.identifiers.addAll(identifiers);
        return this;
    }

    public @NotNull ListenerBuilder<T> andAction(@NotNull EventConsumer<T> action) {
        this.consumer = consumer.and(action);
        return this;
    }

    public @NotNull ListenerBuilder<T> consumer(@NotNull EventConsumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    public @NotNull ListenerBuilder<T> disposable() {
        this.disposable = true;
        return this;
    }

    public @NotNull Listener<T> build() {
        if (disposable) {
            consumer = new DisposableEventConsumer<>(consumer);
        }

        return new ConsumerWrapper<>(consumer, identifiers);
    }
}