package com.github.ynverxe.blue.storage.crud.adapter.defaults;

import com.github.ynverxe.blue.storage.crud.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class StringToJsonAdapter implements RawDataAdapter<String, JSONObject> {
  @Override
  public @NotNull JSONObject adaptToNewData(@NotNull String input) {
    return new JSONObject(input);
  }

  @Override
  public @NotNull String adaptToBackingData(@NotNull JSONObject output) {
    return output.toString();
  }
}