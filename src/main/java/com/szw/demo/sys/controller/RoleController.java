package com.szw.demo.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szw.demo.common.vo.Result;
import com.szw.demo.sys.entity.Role;
import com.szw.demo.sys.entity.User;
import com.szw.demo.sys.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        wrapper.orderByDesc(Role::getRoleId);
        Page<Role> page = new Page<>(pageNo,pageSize);
        iRoleService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);
    }

    //@RequestBody 前端json -》user对象
    @ApiOperation("添加角色")
    @PostMapping
    public Result<?> addRole(@RequestBody Role role){
        iRoleService.addRole(role);
        return Result.success("新增角色成功！");
    }

    @ApiOperation("更新角色")
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        iRoleService.updateRole(role);
        return Result.success("修改角色成功！");
    }

    @ApiOperation("根据ID查询角色")
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = iRoleService.getRoleById(id);
        return Result.success(role);
    }

    @ApiOperation("根据ID删除角色")
    @DeleteMapping("/{id}")
    public Result<Role> deleteRoleById(@PathVariable("id") Integer id){
        iRoleService.deleteRoleById(id);
        return Result.success("删除角色成功！");
    }
    
    @ApiOperation("查询所有角色")
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = iRoleService.list();
        return Result.success(roleList);
    }

}
