package com.github.ynverxe.blue.storage.model;

import org.jetbrains.annotations.NotNull;

/**
 * An interface in charge of serializing and deserializing a model.
 *
 * @param <D> The data type that the model will be serialized
 * @param <T> The model type
 */
public interface ModelSerializer<D, T> {

  @NotNull D serializeModel(@NotNull T model) throws Throwable;

  @NotNull T deserializeModel(@NotNull D modelData) throws Throwable;

}