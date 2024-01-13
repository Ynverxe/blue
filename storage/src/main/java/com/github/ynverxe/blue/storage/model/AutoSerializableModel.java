package com.github.ynverxe.blue.storage.model;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a model that can serialize itself.
 *
 * @param <T> The type of data to which the model should be serialized.
 */
public interface AutoSerializableModel<T> {
  @NotNull T serialize() throws Throwable;
}