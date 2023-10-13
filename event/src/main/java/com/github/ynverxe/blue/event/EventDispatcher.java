package com.github.ynverxe.blue.event;

import org.jetbrains.annotations.NotNull;

public interface EventDispatcher<T> {

  @NotNull DispatchResult<T> dispatchEvent(@NotNull T event);

}