package com.szw.demo.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szw.demo.common.vo.Result;
import com.szw.demo.sys.entity.User;
import com.szw.demo.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author szw
 * @since 2023-03-14
 */
@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
//@CrossOrigin 跨域处理 设置后该控制器可以处理跨域请求
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> list = iUserService.list();
        return Result.success(list,"查询成功!");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        Map<String,Object> map = iUserService.login(user);
        if (map != null){
            return Result.success(map);
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @ApiOperation("用户信息")
    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        //根据token获取用户信息:redis
        Map<String,Object> data = iUserService.getUserInfo(token);
        if (data != null){
            return Result.success(data);
        }
        return Result.fail(20003,"登录信息无效请重新登录");
    }

    @ApiOperation("用户退出")
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token ){
        iUserService.logout(token);
        return Result.success();
    }

    @ApiOperation("用户列表")
    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                              @RequestParam(value = "phone",required = false) String phone,
                                              @RequestParam(value = "pageNo") Long pageNo,
                                              @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        wrapper.orderByDesc(User::getId);
        Page<User> page = new Page<>(pageNo,pageSize);
        iUserService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);
    }

    //@RequestBody 前端json -》user对象
    @ApiOperation("添加用户")
    @PostMapping
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        iUserService.addUser(user);
        return Result.success("新增用户成功！");
    }

    @ApiOperation("更新用户")
    @PutMapping
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        iUserService.updateUser(user);
        return Result.success("修改用户成功！");
    }

    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = iUserService.getUserById(id);
        return Result.success(user);
    }

    @ApiOperation("根据ID删除用户")
    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id){
        iUserService.deleteUserById(id);
        return Result.success("删除用户成功！");
    }



}
