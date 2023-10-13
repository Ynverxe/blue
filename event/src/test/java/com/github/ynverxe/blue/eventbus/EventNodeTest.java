package com.github.ynverxe.blue.eventbus;

import com.github.ynverxe.blue.eventbus.event.InvalidEvent;
import com.github.ynverxe.blue.eventbus.event.TestEvent;
import com.github.ynverxe.blue.eventbus.event.ValidEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public final class EventNodeTest {

  @Test
  public void testFiltering() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class, event -> !(event instanceof InvalidEvent));

    assertTrue(eventNode.dispatchEvent(new ValidEvent()).dispatched());
    assertFalse(eventNode.dispatchEvent(new InvalidEvent()).dispatched());
  }

  @Test
  public void testAbstractConsuming() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    AtomicInteger firedConsumers = new AtomicInteger();
    eventNode.addEventConsumer(TestEvent.class, EventConsumer.newAbstract((caller, event) -> firedConsumers.incrementAndGet()));

    eventNode.dispatchEvent(new InvalidEvent());
    eventNode.dispatchEvent(new ValidEvent());

    assertEquals(2, firedConsumers.get());
  }

  @Test
  public void testNodeNotify() {
    AtomicInteger firedConsumers = new AtomicInteger();
    EventConsumer defConsumer = (caller, event) -> firedConsumers.incrementAndGet();

    EventNode<TestEvent> head = EventNode.create(TestEvent.class);

    // receives all TestEvent implementations
    EventNode<TestEvent> children = EventNode.create(TestEvent.class);
    children.addEventConsumer(ValidEvent.class, defConsumer);
    children.addEventConsumer(InvalidEvent.class, defConsumer);

    head.addEventConsumer(TestEvent.class, children);

    head.dispatchEvent(new ValidEvent());
    head.dispatchEvent(new InvalidEvent());

    assertEquals(2, firedConsumers.get());
  }

  @Test
  public void testNoConcurrentException() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    assertDoesNotThrow(() -> {
      eventNode.addEventConsumer(TestEvent.class, (caller, event) -> {
      });

      eventNode.removeEventConsumers((type, consumer) -> {
        eventNode.addEventConsumer(TestEvent.class, (caller, event) -> {
        });
        eventNode.addEventConsumer(TestEvent.class, (caller, event) -> {
        });
        return false;
      });
    });
  }
}