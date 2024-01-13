package com.github.ynverxe.blue.storage.crud.types;

import com.github.ynverxe.blue.storage.crud.AdaptedCrudRepository;
import com.github.ynverxe.blue.storage.crud.CrudRepository;
import com.github.ynverxe.blue.storage.crud.adapter.RawDataAdapter;
import com.github.ynverxe.blue.storage.crud.adapter.defaults.StringToJsonAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface TextCrudRepository extends CrudRepository<String> {

  default JsonCrudRepository.@NotNull Adapter<String> toJsonRepository(@NotNull RawDataAdapter<String, JSONObject> adapter) {
    return JsonCrudRepository.of(this, adapter);
  }

  default JsonCrudRepository.@NotNull Adapter<String> toJsonRepository() {
    return toJsonRepository(new StringToJsonAdapter());
  }

  class Adapter<I> extends AdaptedCrudRepository<I, String> implements TextCrudRepository {

    public Adapter(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, String> adapter) {
      super(backing, adapter);
    }
  }

  static <I> @NotNull Adapter<I> of(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, String> adapter) {
    return new Adapter<>(backing, adapter);
  }
}