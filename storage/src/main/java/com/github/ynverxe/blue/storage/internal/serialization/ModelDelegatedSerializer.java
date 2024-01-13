package com.github.ynverxe.blue.storage.internal.serialization;

import com.github.ynverxe.blue.storage.model.AutoSerializableModel;
import com.github.ynverxe.blue.storage.model.ModelSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A serializer that can be used to delegate the serialization process to the
 * model itself.
 * For deserialization process, you can provide a function to deserialize the model manually
 * or pass instead a {@link ReflectiveModelDeserializer}.
 */
public class ModelDelegatedSerializer<D, T extends AutoSerializableModel<D>> implements ModelSerializer<D, T> {

  private final Function<D, T> deserializerFunction;

  public ModelDelegatedSerializer(@NotNull Function<D, T> deserializerFunction) {
    this.deserializerFunction = deserializerFunction;
  }

  @Override
  public @NotNull D serializeModel(@NotNull T model) throws Throwable {
    return model.serialize();
  }

  @Override
  public @NotNull T deserializeModel(@NotNull D modelData) throws Throwable {
    return deserializerFunction.apply(modelData);
  }

  public static <D, T extends AutoSerializableModel<D>> ModelDelegatedSerializer<D, T> of(@NotNull Function<D, T> deserializerFunction) {
    return new ModelDelegatedSerializer<>(deserializerFunction);
  }

  public static <D, T extends AutoSerializableModel<D>> ModelDelegatedSerializer<D, T> reflective(@NotNull Class<T> modelClass) {
    return new ModelDelegatedSerializer<>(new ReflectiveModelDeserializer<>(modelClass));
  }
}