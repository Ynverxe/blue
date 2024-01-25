package com.github.ynverxe.blue.rawdata.value.defaults;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class SerializableTypes {

  public static final Set<Class<?>> SERIALIZABLE_DATA_TYPES;

  static {
    Set<Class<?>> serializableTypes = new LinkedHashSet<>();

    serializableTypes.add(Number.class);
    serializableTypes.add(String.class);
    serializableTypes.add(char.class);
    serializableTypes.add(boolean.class);
    serializableTypes.add(Boolean.class);
    serializableTypes.add(Character.class);

    SERIALIZABLE_DATA_TYPES = Collections.unmodifiableSet(serializableTypes);
  }

  private SerializableTypes() {}

  public static boolean isSerializable(@NotNull Class<?> clazz) {
    for (Class<?> primitiveDataType : SERIALIZABLE_DATA_TYPES) {
      if (primitiveDataType.isAssignableFrom(clazz)) return true;
    }

    return false;
  }
}