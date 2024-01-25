package com.github.ynverxe.blue.rawdata.model;

import org.jetbrains.annotations.NotNull;

public interface Copyable {

  @NotNull Object newCopy();

}