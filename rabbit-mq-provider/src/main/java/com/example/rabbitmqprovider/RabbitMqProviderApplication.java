package com.example.rabbitmqprovider;

import com.mysql.cj.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@RestController
@Slf4j
public class RabbitMqProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqProviderApplication.class, args);
    }

    @Autowired
    private RabbitTemplate template;

    @GetMapping("send")
    public String send() {
        try {
            template.convertAndSend("test", new Date().toString());
            log.info("发送成功");
        } catch (Exception e) {
            log.error("发送mq 失败---->>>>{}", e.getMessage());
        }
        return "123";
    }

}
