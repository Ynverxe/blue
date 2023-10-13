package com.github.ynverxe.blue.eventbus;

import org.jetbrains.annotations.NotNull;

public interface Listenable<T> {

  @NotNull EventNode<T> eventNode();

}