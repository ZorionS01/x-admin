package com.szw.demo.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szw.demo.common.utils.JwtUtil;
import com.szw.demo.sys.entity.Menu;
import com.szw.demo.sys.entity.User;
import com.szw.demo.sys.entity.UserRole;
import com.szw.demo.sys.mapper.UserMapper;
import com.szw.demo.sys.mapper.UserRoleMapper;
import com.szw.demo.sys.service.IMenuService;
import com.szw.demo.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szw
 * @since 2023-03-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private RedisTemplate redisTemplate;
   /* @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());
        //根据用户和密码查询
        User loginUser = this.baseMapper.selectOne(wrapper);
        if (loginUser != null){
            //暂时用UUID,最终是jwt
            String key = "user:"+ UUID.randomUUID();

            //存入redis
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            //返回数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("token",key);
            return map;
        }


        //结果不为空,则生成token 将用户信息存入redis
        return null;
    }*/
   @Override
   public Map<String, Object> login(User user) {

       LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
       wrapper.eq(User::getUsername,user.getUsername());
       //根据用户查 结果不为空
       User loginUser = this.baseMapper.selectOne(wrapper);
       if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
           //暂时用UUID,最终是jwt
//           String key = "user:"+ UUID.randomUUID();

           //存入redis
           loginUser.setPassword(null);
//           redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

           //创建jwt
           String token = jwtUtil.createToken(loginUser);
           //返回数据
           HashMap<String, Object> map = new HashMap<>();
           map.put("token",token);
           return map;
       }


       //结果不为空,则生成token 将用户信息存入redis
       return null;
   }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        //根据token获取用户信息:redis
//        Object o = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginUser != null){
//            User loginUser = JSON.parseObject(JSON.toJSONString(o),User.class);
            Map<String, Object> data = new HashMap<>();

            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());

            //角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles",roleList);

            //权限列表
            List<Menu> menuList = iMenuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);

    }

    @Override
    @Transactional
    public void addUser(User user) {
        //写入用户表
        this.baseMapper.insert(user);
        //写入用户角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer integer : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),integer));
            }
        }
    }

    @Override
    @Transactional
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);

        List<Integer> roleIdList = userRoleList.stream()
                .map(userRole -> {return userRole.getRoleId();}).collect(Collectors.toList());
        user.setRoleIdList(roleIdList);

        return user;
   }

    @Override
    @Transactional
    public void updateUser(User user) {
       //更新用户表
        this.baseMapper.updateById(user);
        //清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        //设置新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer integer : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),integer));
            }
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        this.baseMapper.deleteById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);
    }
}
