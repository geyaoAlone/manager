package com.geyao.manager.common.service;

import com.geyao.manager.common.dataobject.table.SysMerchant;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.db.mysql.mapper.SysMerchantMapper;
import com.geyao.manager.common.db.mysql.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMerchantMapper sysMerchantMapper;

    public SysMerchant querySysMerchantInfo(String moduleCode,String merchantCode){
        return sysMerchantMapper.querySysMerchant(moduleCode,merchantCode);
    }

    public SysUser querySysUser(String moduleCode,String merchantCode,String username,String mobile){
        String thirdParam = StringUtils.isBlank(username) ? mobile : username;
        return sysUserMapper.querySysUser(moduleCode,merchantCode,thirdParam);
    }



}
