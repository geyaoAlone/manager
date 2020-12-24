package com.geyao.manager.common.web.impl;

import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.web.SmsInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SmsController implements SmsInterface {


    @Override
    public ResultVO sendValidateCode(String mobile, String type) {
        return null;
    }

    @Override
    public ResultVO checklidateCode(String mobile, String type, String validateCode) {
        return null;
    }
}
