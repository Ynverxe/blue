package com.github.ynverxe.blue.eventbus.consumer;

import com.github.ynverxe.blue.eventbus.EventConsumer;

import java.util.function.Consumer;

public interface ExpirableConsumer<T> extends EventConsumer<T> {

  void handleExpiration(Consumer<ExpirableConsumer<T>> action);

  boolean expired();

  void expire();

  abstract class Impl<T> implements ExpirableConsumer<T> {
    protected Consumer<ExpirableConsumer<T>> expirationAction = listener -> {
    };

    @Override
    public void handleExpiration(Consumer<ExpirableConsumer<T>> action) {
      if (expired()) return;

      this.expirationAction = expirationAction.andThen(action);
    }

    @Override
    public boolean expired() {
      return this.expirationAction == null;
    }
  }
}