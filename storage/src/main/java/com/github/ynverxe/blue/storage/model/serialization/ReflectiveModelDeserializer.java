package com.github.ynverxe.blue.storage.model.serialization;

import com.github.ynverxe.blue.storage.annotation.DeserializationVia;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.function.Function;

public class ReflectiveModelDeserializer<D, T> implements Function<D, T> {

  private final AccessibleObject reflectiveObject;

  private final Class<T> modelClass;
  private final Annotation deserializationVia;

  public ReflectiveModelDeserializer(Class<T> modelClass) {
    this.modelClass = modelClass;
    this.deserializationVia = matchAnnotation(modelClass);

    try {
      this.reflectiveObject = inspect();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull T apply(@NotNull D data) {
    try {
      if (reflectiveObject instanceof Constructor) {
        return ((Constructor<T>) reflectiveObject).newInstance(data);
      }

      return (T) ((Method) reflectiveObject).invoke(null, data);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private AccessibleObject inspect() throws NoSuchMethodException {
    AccessibleObject accessibleObject;

    if (deserializationVia instanceof DeserializationVia.Constructor) {
      DeserializationVia.Constructor constructorData = (DeserializationVia.Constructor) deserializationVia;

      accessibleObject = modelClass.getDeclaredConstructor(constructorData.argumentType());
    } else {
      DeserializationVia.Method methodData = (DeserializationVia.Method) deserializationVia;
      Method method = modelClass.getDeclaredMethod(methodData.name(), methodData.argumentType());

      if (!Modifier.isStatic(method.getModifiers())) {
        throw new IllegalStateException("Method '" + method + "' is not static");
      }

      if (!modelClass.isAssignableFrom(method.getReturnType())) {
        throw new IllegalStateException("Method '" + method + "' doesn't returns an instance of model '" + modelClass + "'");
      }

      accessibleObject = method;
    }

    if (!accessibleObject.isAccessible()) {
      accessibleObject.setAccessible(true);
    }

    return accessibleObject;
  }

  private static Annotation matchAnnotation(Class<?> clazz) {
    for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
      Class<?> type = declaredAnnotation.annotationType();

      if (type == DeserializationVia.Constructor.class || type == DeserializationVia.Method.class) {
        return declaredAnnotation;
      }
    }

    throw new IllegalStateException("Class '" + clazz + "' doesn't have an specified deserialization via");
  }
}