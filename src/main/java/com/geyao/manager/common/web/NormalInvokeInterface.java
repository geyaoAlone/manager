package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface NormalInvokeInterface {


    @RequestMapping("/get")
    public ResultVO invoke_get();

    @RequestMapping("/post")
    public ResultVO invoke_post(CommonInvokeVO vo) ;
}
