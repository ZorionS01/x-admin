package com.szw.demo.sys.mapper;

import com.szw.demo.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szw
 * @since 2023-03-14
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

     List<Integer> getMenuIdListByRoleId(Integer roleId);
}
