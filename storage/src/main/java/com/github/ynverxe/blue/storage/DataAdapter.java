package com.github.ynverxe.storage4all;

import org.jetbrains.annotations.NotNull;

public interface DataAdapter<I, O> {

  @NotNull
  I adaptFromOutput(@NotNull O outputData) throws Exception;

  @NotNull
  O adaptFromInput(@NotNull I inputData) throws Exception;
}
