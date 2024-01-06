package com.github.ynverxe.blue.event.consumer.complex.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConsumerProperties extends ConsumerPropertiesView {

  public ConsumerProperties(@NotNull Map<String, Object> properties) {
    super(properties);
  }

  public @Nullable Object setProperty(@NotNull String key, @Nullable Object value) {
    return properties.put(key, value);
  }

  public @NotNull ConsumerPropertiesView newView() {
    return new ConsumerPropertiesView(properties);
  }
}