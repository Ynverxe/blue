package com.github.ynverxe.storage4all.defaults;

import com.github.ynverxe.xenov.common.storage.DatabaseHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.*;

public class DirectoryDataBaseHandler implements DatabaseHandler<byte[]> {

  private @NotNull File directory;
  private @NotNull String format;
  private @NotNull FilenameFilter filenameFilter;

  public DirectoryDataBaseHandler(@NotNull File directory, @NotNull String format)
    throws IOException {
    createDirectories(directory.toPath());
    this.directory = directory;
    if (!format.startsWith(".")) {
      format = "." + format;
    }
    String finalFormat = format;
    this.format = format;
    this.filenameFilter = (dir, name) -> name.endsWith(finalFormat);
  }

  @Override
  public void saveData(@NotNull String key, byte @NotNull [] data) throws IOException {
    File file = newFile(key);
    if (!file.exists()) createFile(file.toPath());

    write(file.toPath(), data);
  }

  @Override
  public boolean deleteData(@NotNull String key) {
    return newFile(key).delete();
  }

  @Override
  public boolean exists(@NotNull String key) {
    return newFile(key).exists();
  }

  @Override
  public byte @Nullable [] findData(@NotNull String key) throws IOException {
    File file = newFile(key);

    if (!file.exists()) return null;

    return readAllBytes(file.toPath());
  }

  @Override
  public @NotNull List<String> collectKeys() {
    List<String> keys = new ArrayList<>();

    File[] files = directory.listFiles(filenameFilter);

    if (files != null) {
      Arrays.stream(files).forEach(file -> keys.add(file.getName().split("\\.")[0]));
    }

    return keys;
  }

  private File newFile(String key) {
    return new File(directory, key + format);
  }

  @Override
  public void close() throws IOException {
    directory = null;
    format = null;
    filenameFilter = null;
  }
}
