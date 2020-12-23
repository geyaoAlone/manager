package com.geyao.manager.common.web;

import com.geyao.manager.common.dataobject.ResultVO;
import com.geyao.manager.common.dataobject.SysUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 安全服务接口
 */
@RequestMapping("/security")
public interface SecurityAuthService {

    @RequestMapping("/checkToken")
    public ResultVO checkToken(String token);

    @RequestMapping("/refreshToken")
    public ResultVO refreshToken(String token);

    @RequestMapping("/login")
    public ResultVO login(@RequestBody SysUser user);



}
