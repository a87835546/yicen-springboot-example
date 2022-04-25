package com.example.websocketexample;

import com.example.websocketexample.sender.Result;
import com.example.websocketexample.sender.SendEntityVo;
import com.example.websocketexample.websocket.WebSocketSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@Slf4j
@RestController()
public class WebsocketExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketExampleApplication.class, args);
    }

    @GetMapping("ws")
    @ResponseBody
    public String ws(Integer userId, HttpServletRequest request) {
        request.setAttribute("userId", userId);
        return "ws";
    }

    @PostMapping("push")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Result send(HttpServletRequest requestContext, @RequestBody SendEntityVo sendEntityVo) {
        log.info("接受到的请求参数----->>>>{}", sendEntityVo);
        try {
            WebSocketSupport.tryPush(sendEntityVo);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("用户不在线或者参数异常");
        }
    }
}
