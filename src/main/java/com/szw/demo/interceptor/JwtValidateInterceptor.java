package com.szw.demo.interceptor;

import com.alibaba.fastjson2.JSON;
import com.szw.demo.common.utils.JwtUtil;
import com.szw.demo.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Szw 2001
 * @Date 2023/3/18 21:12
 * @Slogn 致未来的你！
 */
@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        System.out.println(request.getRequestURI()+"待验证:"+token);
        if (token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI()+"放行....");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        log.debug(request.getRequestURI()+"禁止访问...");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.fail(20003,"jwt令牌无效,重新登录")));
        return false;//拦截
    }
}
