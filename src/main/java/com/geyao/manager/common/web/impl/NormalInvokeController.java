package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSON;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.db.mysql.mapper.SysUserMapper;
import com.geyao.manager.common.service.NormalInvokeService;
import com.geyao.manager.common.web.NormalInvokeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class NormalInvokeController extends BaseController implements NormalInvokeInterface {

    @Autowired
    private NormalInvokeService normalInvokeService;

    @Override
    public ResultVO invoke_get() {
        SysUser user = getUser();

        return new ResultVO("调用成功！",user);
    }

    @Override
    public ResultVO invoke_post(CommonInvokeVO vo) {
        SysUser user = getUser();
        logger.info("post invoke params:{}", JSON.toJSONString(vo));

        return new ResultVO("调用成功！",user);
    }
}
