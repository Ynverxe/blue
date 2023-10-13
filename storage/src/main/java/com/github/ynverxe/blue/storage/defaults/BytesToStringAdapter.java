package com.github.ynverxe.blue.storage.defaults;

import com.github.ynverxe.blue.storage.DataAdapter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class BytesToStringAdapter implements DataAdapter<byte[], String> {
  @Override
  public byte @NotNull [] adaptFromOutput(@NotNull String outputData) throws Exception {
    return outputData.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public @NotNull String adaptFromInput(byte @NotNull [] inputData) throws Exception {
    return new String(inputData, StandardCharsets.UTF_8);
  }
}
