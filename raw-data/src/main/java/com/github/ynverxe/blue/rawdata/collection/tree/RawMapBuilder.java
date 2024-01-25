package com.github.ynverxe.blue.rawdata.collection.tree;

import com.github.ynverxe.blue.rawdata.collection.abstraction.builder.AbstractRawCollectionBuilder;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RawMapBuilder extends AbstractRawCollectionBuilder<RawMap, Map<String, RawValue>> {
  protected RawMapBuilder() {
    super(HashMap::new);
  }

  @Override
  public @NotNull RawMap build() {
    RawMap map = new RawMap(backingSupplier.get(), allowNullValues);
    if (contents != null) {
      map.putAll(contents);
    }
    return map;
  }
}