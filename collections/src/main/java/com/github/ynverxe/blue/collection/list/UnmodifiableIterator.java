package com.github.ynverxe.blue.collection;

import java.util.Iterator;

public class UnmodifiableIterator<T> implements Iterator<T> {

  private final Iterator<T> delegate;

  public UnmodifiableIterator(Iterator<T> delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean hasNext() {
    return delegate.hasNext();
  }

  @Override
  public T next() {
    return delegate.next();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Immutable");
  }
}