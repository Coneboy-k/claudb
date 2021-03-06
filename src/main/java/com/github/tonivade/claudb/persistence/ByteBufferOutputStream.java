/*
 * Copyright (c) 2015-2018, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.claudb.persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream {

  private static final int DEFAULT_CAPACITY = 1024;

  private int growing;

  private ByteBuffer buffer;

  public ByteBufferOutputStream() {
    this(DEFAULT_CAPACITY, DEFAULT_CAPACITY);
  }

  public ByteBufferOutputStream(int capacity) {
    this(capacity, capacity);
  }

  public ByteBufferOutputStream(int capacity, int growing) {
    this.buffer = ByteBuffer.allocate(capacity);
    this.growing = growing;
  }

  @Override
  public void write(int b) throws IOException {
    ensureCapacity(1);
    buffer.put((byte) b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    ensureCapacity(len);
    buffer.put(b, off, len);
  }

  private void ensureCapacity(int len) {
    if (buffer.remaining() < len) {
      buffer = growBuffer(len).put(toByteArray());
    }
  }

  private ByteBuffer growBuffer(int len) {
    return ByteBuffer.allocate(buffer.capacity() + Math.max(len, growing));
  }

  public byte[] toByteArray() {
    byte[] array = new byte[buffer.position()];
    buffer.rewind();
    buffer.get(array);
    return array;
  }
}
