package com.geyao.manager.common.web;

import com.alibaba.fastjson.JSONObject;
import com.geyao.manager.common.dataobject.vo.CommonInvokeVO;
import com.geyao.manager.common.dataobject.vo.ResultVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/noAuth")
public interface NoAuthInvokeInterface {

    @RequestMapping("/get/{code}")
    public ResultVO invoke_get(@PathVariable("code") String code);

    @RequestMapping("/post")
    public ResultVO invoke_post(@RequestBody CommonInvokeVO vo);

    @RequestMapping("transfers/get")
    public ResultVO transfers_get();

    @RequestMapping(value = "transfers/post",method = RequestMethod.POST)
    public Object transfers_post(@RequestParam Map map);



}
