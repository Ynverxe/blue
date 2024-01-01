package com.github.ynverxe.blue.collection.list;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class ReadWriteDelegatedList<E> extends AbstractList<E> {

  private final boolean updateModCount;
  private final Lock readLock;
  private final Lock writeLock;
  private final List<E> delegate;

  public ReadWriteDelegatedList(boolean updateModCount, ReadWriteLock lock, List<E> delegate) {
    this.updateModCount = updateModCount;
    this.readLock = lock.readLock();
    this.writeLock = lock.writeLock();
    this.delegate = delegate;
  }

  public ReadWriteDelegatedList(boolean updateModCount, List<E> delegate) {
    this(updateModCount, new ReentrantReadWriteLock(), delegate);
  }

  public ReadWriteDelegatedList(boolean updateModCount) {
    this(updateModCount, new ArrayList<>());
  }

  public ReadWriteDelegatedList() {
    this(true, new ArrayList<>());
  }

  @Override
  public E set(int index, E element) {
    return write(list -> list.set(index, element));
  }

  @Override
  public void add(int index, E element) {
    write(list -> {
      list.add(index, element);
      return null;
    });
  }

  @Override
  public E remove(int index) {
    return write(list -> list.remove(index));
  }

  @Override
  public E get(int index) {
    return read(list -> list.get(index));
  }

  @Override
  public int size() {
    return read(List::size);
  }

  private <T> T write(Function<List<E>, T> function) {
    writeLock.lock();

    try {
      return function.apply(delegate);
    } finally {
      if (updateModCount) modCount++;
      writeLock.unlock();
    }
  }

  private <T> T read(Function<List<E>, T> function) {
    readLock.lock();

    try {
      return function.apply(delegate);
    } finally {
      readLock.unlock();
    }
  }
}
