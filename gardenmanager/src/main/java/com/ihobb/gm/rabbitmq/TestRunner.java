package com.ihobb.gm.rabbitmq;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class TestRunner implements CommandLineRunner {


    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public TestRunner(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("sending message...");
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName, "foo.bar.baz", "hello from other side");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);

    }
}
