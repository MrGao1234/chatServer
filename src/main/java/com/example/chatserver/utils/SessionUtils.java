package com.example.chatserver.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    private SessionUtils(){};

    public static HttpSession getSession(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if( null != requestAttributes ){
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession();
            return session;
        }
        return null;
    }
}
