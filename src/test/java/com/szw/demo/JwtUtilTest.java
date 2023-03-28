package com.szw.demo;

import com.szw.demo.common.utils.JwtUtil;
import com.szw.demo.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Szw 2001
 * @Date 2023/3/18 20:31
 * @Slogn 致未来的你！
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPhone("12313123");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwZjc1YmM5OS1hNmU5LTQ3NzgtYjM4NS03YmMwNjZjMmJiNWQiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzMTMxMjNcIixcInVzZXJuYW1lXCI6XCJ6aGFuZ3NhblwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY3OTE0MjgwOCwiZXhwIjoxNjc5MTQ0NjA4fQ.r9bSdSuMtzPY4x1BJXMx9gsR3T0lm_mTnJkd6gqix0s";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }




}
