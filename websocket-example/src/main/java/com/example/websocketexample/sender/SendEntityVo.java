package com.example.websocketexample.sender;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yicen
 */
@Data
public class SendEntityVo implements Serializable {
    String userId;
    String type;
    String orderId;

    @Override
    public String toString() {
        return "SendEntityVo{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
