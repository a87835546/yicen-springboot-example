package com.example.websocketexample.websocket;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/message_websocket")
@RestController
@Slf4j
public class MsgWebsocketController {

    @OnOpen
    public void onOpen(Session session) {
        // 先鉴权，如果鉴权通过则存储WebsocketSession，否则关闭连接，这里省略了鉴权的代码
        WebSocketSupport.storageSession(session);
        // session.setMaxIdleTimeout(60000); // 可以设置session最大空闲时间
        log.info("session open. ID:" + session.getId());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        WebSocketSupport.releaseSession(session);
        System.out.println("session close. ID:" + session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("get client msg. ID:" + session.getId() + ". msg:" + message);
        try {
            JSONObject jsonObject = JSONObject.fromObject(message);
            log.info("json object {}" + jsonObject);
            WebSocketSupport.storageSession(session, String.valueOf(jsonObject.get("userId")));
        } catch (Exception e) {

            log.error("解析数据异常" + e.getMessage());
        }

        String s = "服务器回复的消息---->>>>" + (new Date()) + "发来的消息---->>>>" + message;
        Map map = new HashMap();
        map.putIfAbsent("data", s);
        map.putIfAbsent("time", new Date());
        WebSocketSupport.push(session, map.toString());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
