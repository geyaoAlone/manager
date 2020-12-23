package com.geyao.manager.common.web.impl;

import com.geyao.manager.common.dataobject.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class BaseController {

    public Logger logger = log;


    public SysUser getUser(){
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            SysUser user = (SysUser) authentication.getPrincipal();
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String checkAuth(){
        return "";
    }



}
