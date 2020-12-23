package com.geyao.manager.common.web.impl;

import com.geyao.manager.common.dataobject.ResultVO;
import com.geyao.manager.common.dataobject.SysUser;

import com.geyao.manager.common.web.SecurityAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityAuthServiceImpl implements SecurityAuthService {


    @Override
    public ResultVO checkToken(String token) {
        return null;
    }

    @Override
    public ResultVO refreshToken(String token) {
        return null;
    }

    @Override
    public ResultVO login(SysUser user) {
        return null;
    }
}
