package com.github.ynverxe.blue.rawdata.collection.abstraction;

import com.github.ynverxe.blue.rawdata.collection.abstraction.builder.RawCollectionBuilder;
import com.github.ynverxe.blue.rawdata.model.Copyable;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link RawCollection} view. Every RawCollection implementation
 * have its view implementation. RawCollectionViews must provide a builder
 * to create a new mutable RawCollection with this view contents.
 *
 * @param <B> The builder collection type
 */
@ApiStatus.NonExtendable
public interface RawCollectionView<B extends RawCollectionBuilder<?, ?>> extends RawValue, Copyable {
  @NotNull B newBuilder();
}