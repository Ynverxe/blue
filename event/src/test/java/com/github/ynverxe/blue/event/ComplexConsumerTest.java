package com.github.ynverxe.blue.event;

import com.github.ynverxe.blue.event.consumer.EventConsumer;
import com.github.ynverxe.blue.event.consumer.complex.ComplexConsumer;
import com.github.ynverxe.blue.event.event.TestEvent;
import com.github.ynverxe.blue.event.event.ValidEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexConsumerTest {

  @Test
  public void testDisposable() {
    EventNode<TestEvent> eventNode = EventNode.create(TestEvent.class);

    EventConsumer<TestEvent> consumer = ComplexConsumer.newBuilder(TestEvent.class)
      .makeAbstract()
      .makeDisposable()
      .backing((caller, event) -> System.out.println("Event '" + event + "' consumed"))
      .build();

    eventNode.addEventConsumer(TestEvent.class, consumer);

    eventNode.dispatchEvent(new ValidEvent());

    assertTrue(eventNode.matchConsumers((type, someConsumer) -> someConsumer == consumer).isEmpty(),
      "Disposable Consumer has not been removed");
  }
}