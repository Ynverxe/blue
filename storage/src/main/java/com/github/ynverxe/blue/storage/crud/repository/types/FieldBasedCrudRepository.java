package com.github.ynverxe.blue.storage.crud.repository.types;

import com.github.ynverxe.blue.storage.crud.repository.AdaptedCrudRepository;
import com.github.ynverxe.blue.storage.crud.repository.CrudRepository;
import com.github.ynverxe.blue.storage.crud.repository.adapter.defaults.FieldMapToJsonAdapter;
import com.github.ynverxe.blue.storage.crud.repository.adapter.RawDataAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;

public interface FieldBasedCrudRepository extends CrudRepository<Map<String, Object>> {

  default JsonCrudRepository.@NotNull Adapter<Map<String, Object>> toJsonRepository(@NotNull RawDataAdapter<Map<String, Object>, JSONObject> adapter) {
    return JsonCrudRepository.of(this, adapter);
  }

  default JsonCrudRepository.@NotNull Adapter<Map<String, Object>> toJsonRepository() {
    return toJsonRepository(new FieldMapToJsonAdapter());
  }

  class Adapter<I> extends AdaptedCrudRepository<I, Map<String, Object>> implements FieldBasedCrudRepository {

    public Adapter(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, Map<String, Object>> adapter) {
      super(backing, adapter);
    }
  }

  static <I> @NotNull Adapter<I> of(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, Map<String, Object>> adapter) {
    return new Adapter<>(backing, adapter);
  }
}