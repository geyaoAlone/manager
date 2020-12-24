package com.geyao.manager.common.dataobject.vo;

import com.geyao.manager.common.constants.SysConstant;
import lombok.Data;


@Data
public class ResultVO {
    private int code;
    private String msg;
    private Object data;

    public ResultVO(int code,String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

   public ResultVO(String msg, Object data){
       this.code = SysConstant.INVOKE_SUCCESS;
       this.msg = msg;
       this.data = data;
   }

    public ResultVO(String msg){
        this.code = SysConstant.INVOKE_FAIL;
        this.msg = msg;
    }


    public ResultVO(int code,String msg){
        this.code = code;
        this.msg = msg;
    }


}
