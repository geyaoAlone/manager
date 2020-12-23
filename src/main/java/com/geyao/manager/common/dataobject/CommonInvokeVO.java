package com.geyao.manager.common.dataobject;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class CommonInvokeVO {
    /**
     * 请求地址：
     * 只需要方法上的PostMapping地址
     */
    private String invokeUrl;

    private JSONObject invokeData;

}
