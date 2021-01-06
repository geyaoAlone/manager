package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSON;
import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ModuleInvokeUrlVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.service.NormalInvokeService;
import com.geyao.manager.common.web.NoAuthInvokeInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class NoAuthInvokeController implements NoAuthInvokeInterface {

    @Autowired
    private NormalInvokeService normalInvokeService;

    @Override
    public ResultVO invoke_get(String code) {
        ResultVO resultVO = normalInvokeService.checkCode(code);
        if(SysConstant.INVOKE_SUCCESS != resultVO.getCode()){
            return resultVO;
        }
        ModuleInvokeUrlVO urlVO = (ModuleInvokeUrlVO)resultVO.getData();
        if(SysConstant.INVOKE_NEED_AUTH == urlVO.getIsAuthUrl()){
            return new ResultVO(SysConstant.INVOKE_FAIL,"访问错误！请求地址错误！");
        }
        return normalInvokeService.getInvoke(urlVO);
    }

    @Override
    public ResultVO invoke_post(CommonInvokeVO vo) {
        System.out.println(JSON.toJSONString(vo));
        String invokeCode = vo.getCode();
        if(StringUtils.isBlank(invokeCode)){
            return new ResultVO(SysConstant.INVOKE_FAIL,"调用失败！code为空");
        }
        ResultVO resultVO = normalInvokeService.checkCode(invokeCode);
        if(SysConstant.INVOKE_SUCCESS != resultVO.getCode()){
            return resultVO;
        }
        ModuleInvokeUrlVO urlVO = (ModuleInvokeUrlVO)resultVO.getData();
        if(SysConstant.INVOKE_NO_AUTH == urlVO.getIsAuthUrl()){
            return new ResultVO(SysConstant.INVOKE_FAIL,"访问错误！请求地址错误！");
        }

        return normalInvokeService.postInvoke(urlVO,vo);
    }
}
