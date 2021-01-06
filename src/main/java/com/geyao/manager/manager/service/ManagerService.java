package com.geyao.manager.manager.service;

import com.geyao.manager.common.db.mysql.mapper.SysMerchantMapper;
import com.geyao.manager.common.db.mysql.mapper.SysModuleMapper;
import com.geyao.manager.common.db.mysql.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private SysModuleMapper sysModuleMapper;
    @Autowired
    private SysMerchantMapper sysMerchantMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

}
