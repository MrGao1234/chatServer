package com.example.chatserver.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class UserInterceptor implements HandlerInterceptor {

    //访问controller前调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,String[]> paramMap = request.getParameterMap();
        //Set<Map.Entry<String,String[]>> entrySet = paramMap.entrySet();
        //Set<String> keySet = paramMap.keySet();
        //String userId = paramMap.get("userKey")[0];

        HttpSession session = request.getSession();
        System.out.println(session.getId());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
