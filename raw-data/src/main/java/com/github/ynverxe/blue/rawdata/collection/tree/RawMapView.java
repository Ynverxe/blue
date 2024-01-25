package com.github.ynverxe.blue.rawdata.collection.tree;

import com.github.ynverxe.blue.rawdata.collection.abstraction.RawCollectionView;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RawMapView extends AbstractMap<String, RawValue>
  implements RawCollectionView<RawMapBuilder>, Cloneable {

  protected final Map<String, RawValue> backing;

  public RawMapView(@NotNull Map<String, RawValue> backing) {
    this.backing = backing;
  }

  @Override
  public @NotNull Set<Entry<String, RawValue>> entrySet() {
    return Collections.unmodifiableSet(backing.entrySet());
  }

  @Override
  public @NotNull RawMapView newCopy() {
    return new RawMapView(backing);
  }

  @Override
  public final @NotNull Object value() {
    return this;
  }

  @Override
  public @NotNull RawMapBuilder newBuilder() {
    return new RawMapBuilder();
  }

  @Override
  public RawMapView clone() {
    try {
      return (RawMapView) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    RawMapView that = (RawMapView) o;
    return Objects.equals(backing, that.backing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), backing);
  }

  public boolean isMutable() {
    return false;
  }

  public static @NotNull RawMapView wrapUnknownMap(@NotNull Map<String, RawValue> map, @NotNull Map<?, ?> unknownMap) {
    unknownMap.forEach((key, value) -> map.put(Objects.toString(key), RawValue.wrapUnknown(value)));
    return new RawMapView(map);
  }

  public static @NotNull RawMapView wrapUnknownMap(@NotNull Map<?, ?> unknownCollection) {
    return wrapUnknownMap(new HashMap<>(), unknownCollection);
  }
}