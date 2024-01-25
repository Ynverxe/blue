package com.github.ynverxe.blue.rawdata.collection.tree;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollection;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class RawMap extends RawMapView implements RawCollection<RawMapView, RawMapBuilder, Map<?, ?>>, RawValue {

  private final boolean allowNullValues;

  RawMap(@NotNull Map<String, RawValue> backing, boolean allowNullValues) {
    super(backing);
    this.allowNullValues = allowNullValues;
  }

  @Override
  public @Nullable RawValue put(@NotNull String key, @UnknownNullability RawValue value) {
    if (!allowNullValues && value == null) {
      throw new IllegalArgumentException("This map doesn't supports null values");
    }

    return backing.put(key, value);
  }

  public @Nullable RawValue putUnknown(@NotNull String key, @UnknownNullability Object value) {
    return put(key, RawValue.wrapUnknown(value));
  }

  @NotNull
  @Override
  public Set<Entry<String, RawValue>> entrySet() {
    return backing.entrySet();
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public @NotNull RawMap newCopy() {
    return (RawMap) clone();
  }

  @Override
  public boolean allowNullValues() {
    return allowNullValues;
  }

  @Override
  public void consumeUnknownSource(@NotNull Map<?, ?> unknownMap) {
    unknownMap.forEach((k, v) -> putUnknown(Objects.toString(unknownMap), v));
  }

  @Override
  public @NotNull RawMapView newView() {
    return new RawMapView(backing);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    RawMap rawMap = (RawMap) o;
    return allowNullValues == rawMap.allowNullValues;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), allowNullValues);
  }

  public static @NotNull RawMapBuilder builder() {
    return new RawMapBuilder();
  }

  public static @NotNull RawMap create() {
    return builder().build();
  }
}