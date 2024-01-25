package com.github.ynverxe.blue.rawdata.collection.abstraction;

import org.jetbrains.annotations.ApiStatus;
import com.github.ynverxe.blue.rawdata.collection.abstraction.builder.RawCollectionBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Collection (can be a list or a map) that only
 * holds {@link com.github.ynverxe.blue.rawdata.value.RawValue} instances.
 * Every RawCollection must have a view implementation implementing {@link RawCollectionView}.
 * RawCollections must have methods to receive individual unknown raw type values
 * and unknown collection types e.g {@link #consumeUnknownSource(Object)}.
 *
 * @param <V> The view implementation for this Collection
 * @param <B> The builder type for this Collection
 * @param <U> The unknown collection type that this Collection can consume
 */
@ApiStatus.NonExtendable
public interface RawCollection<V, B extends RawCollectionBuilder<?, ?>, U> extends RawCollectionView<B> {

  boolean allowNullValues();

  void consumeUnknownSource(@NotNull U unknownSource);

  @NotNull V newView();

}
