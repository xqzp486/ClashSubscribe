package org.xqzp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.xqzp.entity.User;
import org.xqzp.exception.ProxyException;
import org.xqzp.service.UserService;


import java.net.Inet4Address;
import java.net.UnknownHostException;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Value("${server.port}")
    String port;

    @GetMapping("/link/{title}")
    public String getSubscribe(@PathVariable("title") String title){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title);
        User user = userService.getOne(wrapper);
        if(user==null){
            throw new ProxyException(404,"没有这个用户");
        }

        String hostAddr;

        try {
            hostAddr= Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new ProxyException(404,"获取当前主机IP异常");
        }

        String v2Subscribe ="http://"+hostAddr+":"+port+"/v2/getContribute?token="+ user.getToken()+"\n";
        String yamlSubscribe ="http://"+hostAddr+":"+port+"/yaml/getYaml?token="+ user.getToken()+"\n";
        String finalSubscribe = v2Subscribe+yamlSubscribe;
        return finalSubscribe;
    }
}

