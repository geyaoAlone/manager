package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSON;
import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ModuleInvokeUrlVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.dataobject.table.SysUser;
import com.geyao.manager.common.service.NormalInvokeService;
import com.geyao.manager.common.web.AuthInvokeNormalInterface;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthInvokeNormalController extends BaseController implements AuthInvokeNormalInterface {

    @Autowired
    private NormalInvokeService normalInvokeService;

    @Override
    public ResultVO invoke_get(String code) {
        ResultVO resultVO = normalInvokeService.checkCode(code);
        if(SysConstant.INVOKE_SUCCESS != resultVO.getCode()){
            return resultVO;
        }
        ModuleInvokeUrlVO urlVO = (ModuleInvokeUrlVO)resultVO.getData();
        if(SysConstant.INVOKE_NO_AUTH == urlVO.getIsAuthUrl()){
            return new ResultVO(SysConstant.INVOKE_FAIL,"无访问权限：code或请求地址错误！");
        }
        SysUser user = getUser();
        if(StringUtils.isBlank(code)){
            return new ResultVO(SysConstant.INVOKE_FAIL,"调用码为空");
        }
        return normalInvokeService.getInvoke_auth(user,urlVO);
    }

    @Override
    public ResultVO invoke_post(@RequestBody CommonInvokeVO vo) {
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
            return new ResultVO(SysConstant.INVOKE_FAIL,"无访问权限：code或请求地址错误！");
        }
        SysUser user = getUser();
        logger.info("post invoke params:{}", JSON.toJSONString(vo));
        return normalInvokeService.postInvoke_auth(user,urlVO,vo);
    }
}
