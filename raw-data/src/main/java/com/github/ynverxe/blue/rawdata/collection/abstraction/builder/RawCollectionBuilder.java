package com.github.ynverxe.blue.rawdata.collection.abstraction.builder;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A builder for RawCollections.
 *
 * @param <C> The RawCollection type that this builder provides
 * @param <B> The object type that backs the RawCollection
 */
public interface RawCollectionBuilder<C extends RawCollection<?, ?, ?>, B> {

  @Contract("_ -> this")
  @NotNull RawCollectionBuilder<C, B> allowNullValues(boolean allow);

  @Contract("_ -> this")
  @NotNull RawCollectionBuilder<C, B> contents(@NotNull B contents);

  @Contract("_ -> this")
  @NotNull RawCollectionBuilder<C, B> backingProvider(@NotNull Supplier<B> backingProvider);

  @NotNull C build();

}
