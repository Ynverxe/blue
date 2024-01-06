package com.github.ynverxe.blue.event.consumer.complex.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ConsumerPropertiesView {

  protected final Map<String, Object> properties;

  ConsumerPropertiesView(@NotNull Map<String, Object> properties) {
    this.properties = properties;
  }

  public @Nullable Object getProperty(@NotNull String key) {
    return properties.get(key);
  }

  public @NotNull <V> Optional<V> expectPropertyType(@NotNull String key, @NotNull Class<V> expected) {
    Object found = getProperty(key);

    return expected.isInstance(found) ? Optional.of(expected.cast(found)) : Optional.empty();
  }

  public boolean expectValue(@NotNull Object expected) {
    return properties.containsValue(expected);
  }

  public @NotNull Map<String, Object> propertiesMapView() {
    return Collections.unmodifiableMap(properties);
  }

}