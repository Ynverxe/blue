package com.github.ynverxe.blue.storage.internal.serialization;

import com.github.ynverxe.blue.storage.model.AutoSerializableModel;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class ModelDelegatedSerializer<T extends AutoSerializableModel<?>> implements AutoSerializableModel<T> {

  
  @Override
  public @NotNull T serialize() throws Throwable {
    return null;
  }
}