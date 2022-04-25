package com.example.rabbitmqconsumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
@Slf4j
public class RabbitMqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqConsumerApplication.class, args);
    }


    @RabbitListener(queues = {"test"})
    @RabbitHandler
    void listen(Message message) {
        try {
            String str = new String(message.getBody(), "UTF-8");

            log.info("接受到的消息---->>>>>{}", str);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("解析字符串异常----?>>{}",e.getMessage());
        }


    }
}
