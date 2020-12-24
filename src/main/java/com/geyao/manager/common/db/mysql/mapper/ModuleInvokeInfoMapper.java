package com.geyao.manager.common.db.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geyao.manager.common.dataobject.table.ModuleInvokeInfo;
import com.geyao.manager.common.dataobject.table.SysMerchant;
import com.geyao.manager.common.dataobject.vo.ModuleInvokeUrlVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ModuleInvokeInfoMapper extends BaseMapper<ModuleInvokeInfo> {

    @Select("select t.*,t1.base_url from t_module_invoke_info t,t_sys_module t1 where t.module_code = t1.module_code and t1.invoke_code = #{invokeCode} and t.status = 1 and t1.status=1")
    ModuleInvokeUrlVO queryModuleInvokeUrl(String invokeCode);
}
