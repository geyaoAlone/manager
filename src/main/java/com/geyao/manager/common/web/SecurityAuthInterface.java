package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.vo.LoginVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 安全服务接口
 */
@RequestMapping("/security")
public interface SecurityAuthInterface {

    @RequestMapping("/test")
    public ResultVO test(String str);

    @RequestMapping("/checkToken")
    public ResultVO checkToken(String token);

    @RequestMapping("/refreshToken")
    public ResultVO refreshToken(String token);

    @RequestMapping("/login")
    public ResultVO login(@RequestBody LoginVO loginVO);



}
