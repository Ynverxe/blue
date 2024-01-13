package com.github.ynverxe.blue.storage.crud.adapter.defaults;

import com.github.ynverxe.blue.storage.crud.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public class JsonToFieldMapAdapter implements RawDataAdapter<JSONObject, Map<String, Object>> {
  @Override
  public @NotNull Map<String, Object> adaptToNewData(@NotNull JSONObject input) {
    return input.toMap();
  }

  @Override
  public @NotNull JSONObject adaptToBackingData(@NotNull Map<String, Object> output) {
    return new JSONObject(output);
  }
}