package com.github.ynverxe.blue.storage.crud.repository.factory;

import com.github.ynverxe.blue.storage.crud.repository.CrudRepository;
import com.github.ynverxe.blue.storage.crud.repository.factory.context.CrudRepositoryParameters;
import org.jetbrains.annotations.NotNull;

public interface CrudRepositoryFactory<T extends CrudRepository<?>> {

  @NotNull T createRepository(@NotNull CrudRepositoryParameters parameters) throws Exception;

}