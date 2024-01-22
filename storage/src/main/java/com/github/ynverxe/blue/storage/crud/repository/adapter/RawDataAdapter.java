package com.github.ynverxe.blue.storage.crud.repository.adapter;

import org.jetbrains.annotations.NotNull;

public interface RawDataAdapter<I, O> {

  @NotNull O adaptToNewData(@NotNull I input);

  @NotNull I adaptToBackingData(@NotNull O output);

}