package org.xqzp.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.xqzp.config.KeyConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KeyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = request.getParameter("key");
        if(KeyConfig.APP_SECRET.equals(key)){
            return true;
        }else {
            return false;
        }
    }
}
