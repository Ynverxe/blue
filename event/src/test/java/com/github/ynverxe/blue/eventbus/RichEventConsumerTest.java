package com.github.ynverxe.blue.eventbus;

import com.github.ynverxe.blue.eventbus.consumer.RichEventConsumer;
import com.github.ynverxe.blue.eventbus.event.TestEvent;
import com.github.ynverxe.blue.eventbus.event.ValidEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RichEventConsumerTest {

  @Test
  public void testDisposable() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    RichEventConsumer<TestEvent> consumer = RichEventConsumer.builder(TestEvent.class)
      .abstractHandling(true)
      .disposable(true)
      .consumer((caller, event) -> System.out.println("Event '" + event + "' consumed"))
      .build();

    eventNode.addEventConsumer(TestEvent.class, consumer);

    eventNode.dispatchEvent(new ValidEvent());

    assertTrue(eventNode.matchConsumers((type, someConsumer) -> someConsumer == consumer).isEmpty(),
      "Disposable Consumer has not been removed");
  }
}