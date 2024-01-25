package com.github.ynverxe.blue.rawdata.collection.list;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollectionView;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RawListView extends AbstractList<RawValue>
  implements RawCollectionView<RawListBuilder>, Cloneable {

  protected final @NotNull List<RawValue> backing;

  public RawListView(@NotNull List<RawValue> backing) {
    this.backing = backing;
  }

  @Override
  public @Nullable RawValue get(int index) {
    return backing.get(index);
  }

  @Override
  public int size() {
    return backing.size();
  }

  @Override
  public @NotNull Object value() {
    return this;
  }

  @Override
  public @NotNull RawListView newCopy() {
    return new RawListView(backing);
  }

  @Override
  public @NotNull RawListBuilder newBuilder() {
    return new RawListBuilder();
  }

  @Override
  public RawListView clone() {
    try {
      return (RawListView) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    RawListView rawValues = (RawListView) o;
    return Objects.equals(backing, rawValues.backing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), backing);
  }

  public static @NotNull RawListView wrapUnknownCollection(@NotNull List<RawValue> list, @NotNull Collection<?> unknownCollection) {
    unknownCollection.forEach(element -> list.add(RawValue.wrapUnknown(element)));
    return new RawListView(list);
  }

  public static @NotNull RawListView wrapUnknownCollection(@NotNull Collection<?> unknownCollection) {
    return wrapUnknownCollection(new ArrayList<>(), unknownCollection);
  }
}