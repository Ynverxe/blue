package com.github.ynverxe.blue.rawdata.test;

import com.github.ynverxe.blue.rawdata.collection.tree.RawMap;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class RawMapTest {

  @Test
  public void testClone() {
    RawMap rawMap = RawMap.create();
    rawMap.putUnknown("someValue", "Ynverxe");

    RawMap cloned = rawMap.newCopy();
    assertEquals(rawMap, cloned);
  }

  @Test
  public void testSerialization() throws Exception {
    RawMap rawMap = RawMap.create();
    rawMap.putUnknown("someValue", "Ynverxe");

    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
    objectOutputStream.writeObject(rawMap);

    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);

    assertEquals(rawMap, objectInputStream.readObject());
  }
}