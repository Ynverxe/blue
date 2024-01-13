package com.github.ynverxe.blue.storage.crud.repository.types;

import com.github.ynverxe.blue.storage.crud.repository.AdaptedCrudRepository;
import com.github.ynverxe.blue.storage.crud.repository.CrudRepository;
import com.github.ynverxe.blue.storage.crud.repository.adapter.defaults.JsonToFieldMapAdapter;
import com.github.ynverxe.blue.storage.crud.repository.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public interface JsonCrudRepository extends CrudRepository<JSONObject> {

  default FieldBasedCrudRepository.@NotNull Adapter<JSONObject> toFieldBased(@NotNull RawDataAdapter<JSONObject, Map<String, Object>> adapter) {
    return FieldBasedCrudRepository.of(this, adapter);
  }

  default FieldBasedCrudRepository.@NotNull Adapter<JSONObject> toFieldBased() {
    return toFieldBased(new JsonToFieldMapAdapter());
  }

  class Adapter<I> extends AdaptedCrudRepository<I, JSONObject> implements JsonCrudRepository {

    public Adapter(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, JSONObject> adapter) {
      super(backing, adapter);
    }
  }

  static <I> @NotNull Adapter<I> of(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, JSONObject> adapter) {
    return new Adapter<>(backing, adapter);
  }
}