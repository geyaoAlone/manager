package com.geyao.manager.common.web.impl;

import com.geyao.manager.common.constants.SysConstant;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ModuleInvokeUrlVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.service.NormalInvokeService;
import com.geyao.manager.common.web.NoAuthInvokeInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;

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





    @Override
    public ResultVO transfers_get() {
        return null;
    }

    @Override
    public Object transfers_post(Map map) {
        String param = "";
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String s = iterator.next();
            if(s != null){
                param = s;
            }
        }
        String urlVO = "https://qr.chinaums.com/netpay-route-server/api/";
        log.info("data：{}",param);
        //return new ResultVO("调用成功",param);
        ResultVO vo = normalInvokeService.transfers_post(urlVO,param);
        return vo.getData();
    }

}
