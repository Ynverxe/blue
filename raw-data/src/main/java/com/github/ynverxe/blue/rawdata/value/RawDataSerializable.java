package com.github.ynverxe.blue.rawdata.value;

import org.jetbrains.annotations.NotNull;

public interface RawDataSerializable {

  @NotNull RawValue toRawData();

}