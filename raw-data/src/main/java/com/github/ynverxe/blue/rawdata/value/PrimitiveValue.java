package com.github.ynverxe.blue.rawdata.value;

import com.github.ynverxe.blue.rawdata.value.defaults.SerializableTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PrimitiveValue implements RawValue {

  private final Object value;

  PrimitiveValue(Object value) {
    this.value = value;
  }

  @Override
  public @NotNull Object value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PrimitiveValue that = (PrimitiveValue) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  static @NotNull PrimitiveValue of(@NotNull Object value) {
    Class<?> valueClass = value.getClass();
    if (!SerializableTypes.isSerializable(valueClass)) {
      throw new IllegalArgumentException(valueClass + " isn't a primitive type. See SupportedDataTypes");
    }

    return new PrimitiveValue(value);
  }
}