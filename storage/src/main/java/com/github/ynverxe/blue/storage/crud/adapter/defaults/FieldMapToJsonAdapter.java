package com.github.ynverxe.blue.storage.crud.adapter.defaults;

import com.github.ynverxe.blue.storage.crud.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public class FieldMapToJsonAdapter implements RawDataAdapter<Map<String, Object>, JSONObject> {
  @Override
  public @NotNull JSONObject adaptToNewData(@NotNull Map<String, Object> input) {
    return new JSONObject(input);
  }

  @Override
  public @NotNull Map<String, Object> adaptToBackingData(@NotNull JSONObject output) {
    return output.toMap();
  }
}