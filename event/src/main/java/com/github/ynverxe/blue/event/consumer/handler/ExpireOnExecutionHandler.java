package com.github.ynverxe.blue.event.consumer.handler;

import com.github.ynverxe.blue.event.EventDispatcher;
import com.github.ynverxe.blue.event.consumer.RichEventConsumer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public class ExpireOnExecutionHandler implements RichEventConsumer.Handler {
  @Override
  public void postConsuming(@NotNull EventDispatcher caller, @NotNull Object event, @NotNull RichEventConsumer holder) {
    holder.expire();
  }
}