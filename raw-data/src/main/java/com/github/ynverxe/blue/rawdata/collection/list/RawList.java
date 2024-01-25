package com.github.ynverxe.blue.rawdata.collection.list;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollection;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class RawList extends RawListView implements RawCollection<RawListView, RawListBuilder, Collection<?>> {

  private final boolean allowNullValues;

  RawList(@NotNull List<RawValue> backing, boolean allowNullValues) {
    super(backing);
    this.allowNullValues = allowNullValues;
  }

  @Override
  public @Nullable RawValue set(int index, @UnknownNullability RawValue element) {
    if (!allowNullValues && element == null) {
      throw new IllegalArgumentException("This list doesn't supports null values");
    }

    return backing.set(index, element);
  }

  public @Nullable RawValue setUnknown(int index, @NotNull Object value) {
    return set(index, RawValue.wrapUnknown(value));
  }

  @Override
  public void add(int index, @UnknownNullability RawValue element) {
    if (!allowNullValues && element == null) {
      throw new IllegalArgumentException("This list doesn't supports null values");
    }

    backing.add(index, element);
  }

  public void addUnknown(int index, @NotNull Object value) {
    add(index, RawValue.wrapUnknown(value));
  }

  public void addUnknown(@UnknownNullability Object value) {
    backing.add(RawValue.wrapUnknown(value));
  }

  @Override
  public RawValue remove(int index) {
    return backing.remove(index);
  }

  @Override
  public boolean allowNullValues() {
    return allowNullValues;
  }

  @Override
  public @NotNull RawList newCopy() {
    return (RawList) clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    RawList rawValues = (RawList) o;
    return allowNullValues == rawValues.allowNullValues;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), allowNullValues);
  }

  @Override
  public void consumeUnknownSource(@NotNull Collection<?> unknownSource) {
    unknownSource.forEach(this::addUnknown);
  }

  @Override
  public @NotNull RawListView newView() {
    return new RawListView(backing);
  }

  public static @NotNull RawListBuilder builder() {
    return new RawListBuilder();
  }

  public static @NotNull RawList create() {
    return builder().build();
  }
}