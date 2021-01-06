package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface AuthInvokeNormalInterface {


    @RequestMapping("/get/{code}")
    public ResultVO invoke_get(@PathVariable("code") String code);

    @RequestMapping("/post")
    public ResultVO invoke_post(CommonInvokeVO vo) ;
}
