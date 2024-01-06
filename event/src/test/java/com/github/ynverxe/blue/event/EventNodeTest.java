package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.event.InvalidEvent;
import com.github.ynverxe.blue.event.event.TestEvent;
import com.github.ynverxe.blue.event.event.ValidEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public final class EventNodeTest {

  /**
   * InvalidEvents shouldn't be dispatched.
   */
  @Test
  public void testFiltering() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class, event -> !(event instanceof InvalidEvent));

    assertTrue(eventNode.dispatchEvent(new ValidEvent()).dispatched());
    assertFalse(eventNode.dispatchEvent(new InvalidEvent()).dispatched());
  }

  /**
   * One consumer that should consume all events that extends from TestEvent.
   */
  @Test
  public void testAbstractConsuming() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    AtomicInteger firedConsumers = new AtomicInteger();
    eventNode.addEventConsumer(TestEvent.class, builder ->
      builder.backing((caller, event) -> firedConsumers.incrementAndGet()).makeAbstract());

    eventNode.dispatchEvent(new InvalidEvent());
    eventNode.dispatchEvent(new ValidEvent());

    assertEquals(2, firedConsumers.get());
  }

  /**
   * One event node that should consume all events that extends from TestEvent.
   */
  @Test
  public void testNodeNotify() {
    AtomicInteger firedConsumers = new AtomicInteger();
    EventConsumer defConsumer = (caller, event) -> firedConsumers.incrementAndGet();

    EventNode<TestEvent> head = EventNode.create(TestEvent.class);

    // receives all TestEvent implementations
    EventNode<TestEvent> children = EventNode.newBuilder(TestEvent.class)
        .makeAbstract().build();

    children.addEventConsumer(ValidEvent.class, defConsumer);
    children.addEventConsumer(InvalidEvent.class, defConsumer);

    head.addEventConsumer(TestEvent.class, children);

    head.dispatchEvent(new ValidEvent());
    head.dispatchEvent(new InvalidEvent());

    assertEquals(2, firedConsumers.get());
  }

  /**
   * EventNodes stores his consumers into a CopyOnWriteArrayList to be
   * safe-thread.
   */
  @Test
  public void testNoConcurrentException() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    assertDoesNotThrow(() -> {
      eventNode.addEventConsumer(TestEvent.class, (caller, event) -> { // add consumer to removeEventConsumers works
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

  @Test
  public void testGlobalConsumerExecution() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);
    AtomicBoolean executed = new AtomicBoolean();
    eventNode.addGlobalConsumer((caller, event) -> executed.set(true));
    eventNode.dispatchEvent(new ValidEvent());
    assertTrue(executed.get());
  }
}