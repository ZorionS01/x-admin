package com.szw.demo.sys.mapper;

import com.szw.demo.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author szw
 * @since 2023-03-14
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuListByUserId(@Param("userId") Integer userId, @Param("pid")Integer pid);

}
