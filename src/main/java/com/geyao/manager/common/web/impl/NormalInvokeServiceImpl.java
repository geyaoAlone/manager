package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSON;
import com.geyao.manager.common.dataobject.CommonInvokeVO;
import com.geyao.manager.common.dataobject.ResultVO;
import com.geyao.manager.common.dataobject.SysUser;
import com.geyao.manager.common.db.mysql.mapper.SysUserMapper;
import com.geyao.manager.common.web.NormalInvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class NormalInvokeServiceImpl extends BaseController implements NormalInvokeService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Override
    public ResultVO invoke_get() {
        SysUser user = getUser();
        user = sysUserMapper.querySysUser("barbershop","jyrs","18287181006");
        return new ResultVO("调用成功！",user);
    }

    @Override
    public ResultVO invoke_post(CommonInvokeVO vo) {
        SysUser user = getUser();
        logger.info("post invoke params:{}", JSON.toJSONString(vo));
        return new ResultVO("调用成功！",user);
    }
}
