package com.github.ynverxe.blue.rawdata.collection.list;

import com.github.ynverxe.blue.rawdata.collection.abstraction.builder.AbstractRawCollectionBuilder;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RawListBuilder extends AbstractRawCollectionBuilder<RawList, List<RawValue>> {

  public RawListBuilder() {
    super(ArrayList::new);
  }

  @Override
  public @NotNull RawList build() {
    RawList list = new RawList(backingSupplier.get(), allowNullValues);
    if (contents != null) {
      list.addAll(contents);
    }
    return list;
  }
}