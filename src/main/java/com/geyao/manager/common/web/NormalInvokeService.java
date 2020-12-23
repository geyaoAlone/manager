package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.CommonInvokeVO;
import com.geyao.manager.common.dataobject.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface NormalInvokeService {


    @RequestMapping("/get")
    public ResultVO invoke_get();

    @RequestMapping("/post")
    public ResultVO invoke_post(CommonInvokeVO vo) ;
}
