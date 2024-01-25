package com.github.ynverxe.blue.rawdata.value;

import com.github.ynverxe.blue.rawdata.collection.list.RawList;
import com.github.ynverxe.blue.rawdata.collection.list.RawListView;
import com.github.ynverxe.blue.rawdata.collection.tree.RawMap;
import com.github.ynverxe.blue.rawdata.collection.tree.RawMapView;
import com.github.ynverxe.blue.rawdata.util.text.JsonRepresenter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@ApiStatus.NonExtendable
public interface RawValue extends Serializable {

  /**
   * This value cast as double.
   */
  default double asDouble() {
    return asNumber().doubleValue();
  }

  /**
   * This value cast as int.
   */
  default int asInt() {
    return  asNumber().intValue();
  }

  /**
   * This value cast as float.
   */
  default float asFloat() {
    return  asNumber().floatValue();
  }

  /**
   * This value cast as long.
   */
  default long asLong() {
    return asNumber().longValue();
  }

  /**
   * This value cast as byte.
   */
  default byte asByte() {
    return asNumber().byteValue();
  }

  /**
   * This value cast as short.
   */
  default short asShort() {
    return asNumber().shortValue();
  }

  /**
   * This value cast as number.
   */
  default @NotNull Number asNumber() {
    return castTo(value(), Number.class);
  }

  /**
   * This value cast as boolean.
   */
  default boolean asBoolean() {
    return castTo(value(), boolean.class);
  }

  /**
   * This value cast as string.
   */
  default @NotNull String asString() {
    return castTo(value(), String.class);
  }

  /**
   * This value cast as char.
   */
  default char asChar() {
    return castTo(value(), char.class);
  }

  default @NotNull RawMapView asMapView() {
    return castTo(value(), RawMapView.class);
  }

  default @NotNull RawMap asMap() {
    return castTo(value(), RawMap.class);
  }

  default @NotNull RawListView asListView() {
    return castTo(value(), RawListView.class);
  }

  default @NotNull RawList asList() {
    return castTo(value(), RawList.class);
  }

  default boolean isPrimitive() {
    return this instanceof PrimitiveValue;
  }

  default boolean isMap() {
    return this instanceof RawMap;
  }

  default boolean isMapView() {
    return this instanceof RawMapView;
  }

  default boolean isList() {
    return this instanceof RawList;
  }

  default boolean isListView() {
    return this instanceof RawListView;
  }

  default @NotNull String asJsonRepresentation(@NotNull JsonRepresenter representer) {
    return representer.toJson(this);
  }

  default @NotNull String asJsonRepresentation() {
    return asJsonRepresentation(new JsonRepresenter(2));
  }

  /**
   * The value that this object holds;
   */
  @NotNull Object value();

  @ApiStatus.Internal
  static <T> T castTo(@NotNull Object value, @NotNull Class<T> expected) {
    return expected.cast(value);
  }

  static @NotNull PrimitiveValue primitive(@NotNull Object value) {
    return PrimitiveValue.of(value);
  }

  static @NotNull RawValue wrapUnknown(@NotNull Object value) {
    if (value instanceof Map) {
      RawMap map = RawMap.create();
      map.consumeUnknownSource((Map<?, ?>) value);
    }

    if (value instanceof Collection) {
      RawList rawList = RawList.create();
      rawList.consumeUnknownSource((Collection<?>) value);
    }

    if (value instanceof RawValue) {
      return ((RawValue) value);
    }

    return primitive(value);
  }
}