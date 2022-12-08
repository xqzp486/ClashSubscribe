package org.xqzp.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xqzp.entity.User;
import org.xqzp.exception.ProxyException;
import org.xqzp.service.UserService;
import org.xqzp.utils.JwtUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证token是否有效
        if(JwtUtils.checkToken(request)){
            String uuid = JwtUtils.getUuidByJwtToken(request);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("uuid",uuid);
            long count = userService.count(wrapper);
            if(count>0){
                return true;
            }else {
                throw new ProxyException(404,"用户不存在");
            }
        }else {
            return false;
        }
    }
}
