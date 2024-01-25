package com.github.ynverxe.blue.rawdata.util.text;

import com.github.ynverxe.blue.rawdata.collection.list.RawListView;
import com.github.ynverxe.blue.rawdata.collection.tree.RawMapView;
import com.github.ynverxe.blue.rawdata.value.RawValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public final class JsonRepresenter {

  private static final char ARRAY_OPENER = '[', ARRAY_CLOSER = ']';
  private static final char SEPARATOR = ',';
  private static final char LINE_SEPARATOR = '\n';
  private static final char TREE_OPENER = '{', TREE_CLOSER = '}';

  private final int minElementsToCollapse;
  private final int indentFactor;

  public JsonRepresenter(int minElementsToCollapse, int indentFactor) {
    this.minElementsToCollapse = minElementsToCollapse;
    this.indentFactor = indentFactor;

    if (indentFactor < 0) {
      throw new IllegalArgumentException("indentFactor < 0");
    }

    if (minElementsToCollapse < 0) {
      throw new IllegalArgumentException("minElementsToCollapse < 0");
    }
  }

  public JsonRepresenter(int indentFactor) {
    this(0, indentFactor);
  }

  public @NotNull String toJson(@Nullable RawValue value) {
    StringBuilder builder = new StringBuilder();
    toJson(builder, value);
    return builder.toString();
  }

  public void toJson(@NotNull StringBuilder builder, @Nullable RawValue value) {
    asJson(builder, value, 0);
  }

  private void asJson(@NotNull StringBuilder builder, @Nullable RawValue value, int indent) {
    if (value == null) {
      builder.append("null");
      return;
    }

    if (value.isListView()) {
      asJsonArray(builder, value.asListView(), indent);
      return;
    }

    if (value.isMapView()) {
      asJsonTree(builder, value.asMapView(), indent);
      return;
    }

    if (value.value() instanceof String) {
      builder.append(enclose(value.asString(), '"'));
      return;
    }

    builder.append(value.value());
  }

  private void asJsonArray(@NotNull StringBuilder builder, @NotNull RawListView list, int indent) {
    writeStructure(builder, list.size(), new BiConsumer<Integer, StringBuilder>() {
      int index = 0;
      @Override
      public void accept(Integer indent, StringBuilder builder) {
        RawValue value = list.get(index++);
        asJson(builder, value, indent);
      }
    }, indent, ARRAY_OPENER, ARRAY_CLOSER);
  }

  private void asJsonTree(@NotNull StringBuilder builder, @NotNull RawMapView mapView, int indent) {
    writeStructure(builder, mapView.size(), new BiConsumer<Integer, StringBuilder>() {
      final Iterator<Map.Entry<String, RawValue>> iterator = mapView.entrySet().iterator();

      @Override
      public void accept(Integer indent, StringBuilder builder) {
        Map.Entry<String, RawValue> valueEntry = iterator.next();
        builder.append(enclose(valueEntry.getKey(), '"'))
          .append(": ");
        asJson(builder, valueEntry.getValue(), indent);
      }
    }, indent, TREE_OPENER, TREE_CLOSER);
  }

  private void writeStructure(
    @NotNull StringBuilder builder,
    int elementCount,
    BiConsumer<Integer, StringBuilder> appender,
    int indent,
    char opener,
    char closer
  ) {
    boolean separate = indentFactor != 0 && elementCount < minElementsToCollapse || minElementsToCollapse == -1;
    builder.append(opener);

    int totalIndent = indentFactor + indent;

    for (int index = 0; index < elementCount; index++) {
      boolean last = index + 1 >= elementCount;

      if (separate) {
        indent(builder, totalIndent);
      }

      appender.accept(totalIndent, builder);

      if (!last) {
        builder.append(SEPARATOR).append(' ');
      } else if (separate) {
        indent(builder, indent);
      }
    }

    builder.append(closer);
  }

  private static String enclose(String string, char closer) {
    return closer + string + closer;
  }

  private static void indent(@NotNull StringBuilder builder, int indent) {
    builder.append(LINE_SEPARATOR);
    for (int i = 0; i < indent; i++) {
      builder.append(' ');
    }
  }
}