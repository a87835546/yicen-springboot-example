package com.example.rabbitmqconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author yicen
 */
@Slf4j
public class SpringQueueListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        log.info("接受到的消息---->>>>>{}", message);
    }
}
