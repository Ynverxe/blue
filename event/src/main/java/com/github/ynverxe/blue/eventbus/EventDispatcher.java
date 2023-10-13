package com.github.ynverxe.bus.eventbus;

import org.jetbrains.annotations.NotNull;

public interface EventDispatcher<T> {

  @NotNull DispatchResult<T> dispatchEvent(@NotNull T event);

}