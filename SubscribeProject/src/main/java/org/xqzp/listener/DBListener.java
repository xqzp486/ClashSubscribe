package org.xqzp.listener;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.xqzp.entity.User;
import org.xqzp.service.UserService;
import org.xqzp.utils.JwtUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目启动时执行
 * 如果用户没有token密钥，则自定生成一个token密钥
 * 并将该token密钥加入到数据库
 */
@Component
public class DBListener implements ApplicationRunner {
    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<User> userList = userService.list();

        //原来没有token，现在获取到token的用户List
        List<User> getTokenUserList = userList.stream()
                .filter(user -> StringUtils.isEmpty(user.getToken())||!JwtUtils.checkToken(user.getToken()))
                .map(user -> user.setToken(JwtUtils.getJwtToken(user.getUuid())))
                .collect(Collectors.toList());

        getTokenUserList.forEach(userService::updateById);
    }
}
