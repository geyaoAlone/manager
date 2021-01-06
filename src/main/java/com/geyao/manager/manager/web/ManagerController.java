package com.geyao.manager.manager.web;

import com.geyao.manager.common.dataobject.vo.ResultVO;
import com.geyao.manager.common.web.impl.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController extends BaseController {

    @RequestMapping("/queryModule")
    public ResultVO queryModule(){
        return new ResultVO("",null);
    }

}
