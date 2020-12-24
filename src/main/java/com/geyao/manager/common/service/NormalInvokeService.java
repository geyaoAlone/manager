package com.geyao.manager.common.service;

import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.db.mysql.mapper.ModuleInvokeInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalInvokeService {

    @Autowired
    private ModuleInvokeInfoMapper moduleInvokeInfoMapper;

    public ResultVO postInvoke(){
       return null;
    }
}
