package com.geyao.manager.common.db.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geyao.manager.common.dataobject.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    @Select("select * from t_sys_user where module_code = #{moduleCode} and merchant_code = #{merchantCode} and username= #{username}")
    SysUser querySysUser(String moduleCode,String merchantCode,String username);
}
