package com.github.ynverxe.blue.event;

import org.jetbrains.annotations.NotNull;

public interface Listenable<T> {

  @NotNull EventNode<T> eventNode();

}