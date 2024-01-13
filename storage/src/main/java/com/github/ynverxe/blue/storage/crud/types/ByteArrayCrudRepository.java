package com.github.ynverxe.blue.storage.crud.types;

import com.github.ynverxe.blue.storage.crud.AdaptedCrudRepository;
import com.github.ynverxe.blue.storage.crud.CrudRepository;
import com.github.ynverxe.blue.storage.crud.adapter.RawDataAdapter;
import com.github.ynverxe.blue.storage.crud.adapter.defaults.ByteArrayToStringAdapter;
import org.jetbrains.annotations.NotNull;

public interface ByteArrayCrudRepository extends CrudRepository<byte[]> {

  default TextCrudRepository.@NotNull Adapter<byte[]> toTextRepository(@NotNull RawDataAdapter<byte[], String> adapter) {
    return TextCrudRepository.of(this, adapter);
  }

  default TextCrudRepository.@NotNull Adapter<byte[]> toTextRepository() {
    return toTextRepository(new ByteArrayToStringAdapter());
  }

  class Adapter<I> extends AdaptedCrudRepository<I, byte[]> implements ByteArrayCrudRepository {

    public Adapter(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, byte[]> adapter) {
      super(backing, adapter);
    }
  }

  static <I> @NotNull Adapter<I> of(@NotNull CrudRepository<I> backing, @NotNull RawDataAdapter<I, byte[]> adapter) {
    return new Adapter<>(backing, adapter);
  }
}