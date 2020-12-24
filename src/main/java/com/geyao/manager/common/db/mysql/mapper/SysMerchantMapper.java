package com.geyao.manager.common.db.mysql.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geyao.manager.common.dataobject.table.SysMerchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysMerchantMapper extends BaseMapper<SysMerchant> {


    @Select("select t.* from t_sys_merchant t,t_sys_module t1 where t.module_code = t1.module_code and t1.module_code = #{moduleCode} and t.merchant_code = #{merchantCode} and status=1")
    SysMerchant querySysMerchant(String moduleCode, String merchantCode);
}
