package com.example.websocketexample.websocket;

import com.example.websocketexample.sender.MsgDTO;
import com.example.websocketexample.sender.SendEntityVo;
import com.example.websocketexample.websocket.util.QueryStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketSupport {
    @Autowired
    private static RedisTemplate redisTemplate;

    private static String kSessionRedisPrefix = "session_redis:";

    private static final WsSessionManager sessionManager = new WsSessionManager();

    private WebSocketSupport() {
    }

    /**
     * 尝试向客户端推送消息
     *
     * @param msgDTO
     */
    public static void tryPush(MsgDTO msgDTO) {
        if (msgDTO == null) {
            return;
        }

        String userId = String.valueOf(msgDTO.getReceiverId());
        Session session = sessionManager.get(userId);

        if (session != null && session.isOpen()) {
            push(session, msgDTO.getMsgBody());
        }

    }

    public static void tryPush(SendEntityVo msgDTO) {
        if (msgDTO == null) {
            return;
        }
        try {
            String userId = String.valueOf(msgDTO.getUserId());
            Session session = sessionManager.get(userId);
            log.info("get session by cache --->>>>>>{} session is open --->>>{}", session, session.isOpen());
            JSONObject jsonObject = JSONObject.fromObject(msgDTO);
            if (session != null && session.isOpen()) {
                push(session, jsonObject.toString());
            }
        } catch (Exception e) {
            log.error("推送消息发送异常,{}此用户不在线---->>>{}", msgDTO.getUserId(), e.getMessage());
        }
    }

    public static void push(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void storageSession(Session session) {
        String querystring = session.getQueryString();

        if (null != querystring) {
            Map<String, String> param = QueryStringUtil.parse(querystring);
            String key = param.get("userId");

            sessionManager.save(key, session);
        }
    }

    public static void storageSession(Session session, String userId) {
        redisTemplate.opsForValue().set(kSessionRedisPrefix + userId, session.getId());
        sessionManager.save(userId, session);
    }

    public static Session getSession(String id) {
        return sessionManager.get(id);
    }

    public static void releaseSession(String id) {
        sessionManager.releaseSession(id);
    }

    public static void releaseSession(Session session) {
        String querystring = session.getQueryString();
        if (null != querystring) {
            Map<String, String> param = QueryStringUtil.parse(querystring);
            String key = param.get("userId");

            sessionManager.releaseSession(key);
        }
    }

    private static class WsSessionManager {

        final ConcurrentHashMap<Object, Session> sessionPool = new ConcurrentHashMap();

        void save(Object key, Session session) {
            sessionPool.put(key, session);
            log.info("session pool ----->>>>{}", sessionPool);
        }

        Session get(String key) {
            return sessionPool.get(key);
        }

        boolean haveSession(String key) {
            return sessionPool.containsKey(key);
        }

        void releaseSession(String key) {
            Session session = sessionPool.remove(key);
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
