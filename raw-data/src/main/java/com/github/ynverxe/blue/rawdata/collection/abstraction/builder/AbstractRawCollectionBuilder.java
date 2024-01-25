package com.github.ynverxe.blue.rawdata.collection.abstraction.builder;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollection;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class AbstractRawCollectionBuilder<T extends RawCollection<?, ?, ?>, B> implements RawCollectionBuilder<T, B> {

  protected boolean allowNullValues;
  protected @NotNull Supplier<B> backingSupplier;
  protected B contents;

  protected AbstractRawCollectionBuilder(@NotNull Supplier<B> backingSupplier) {
    this.backingSupplier = backingSupplier;
  }

  @Override
  public @NotNull RawCollectionBuilder<T, B> allowNullValues(boolean allow) {
    this.allowNullValues = allow;
    return this;
  }

  @Override
  public @NotNull RawCollectionBuilder<T, B> contents(@NotNull B contents) {
    this.contents = contents;
    return this;
  }

  @Override
  public @NotNull RawCollectionBuilder<T, B> backingProvider(@NotNull Supplier<B> backingProvider) {
    this.backingSupplier = backingProvider;
    return this;
  }
}
