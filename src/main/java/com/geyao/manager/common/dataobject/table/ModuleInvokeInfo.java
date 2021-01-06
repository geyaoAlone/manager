package com.geyao.manager.common.dataobject.table;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_module_invoke_info")
public class ModuleInvokeInfo implements Serializable {

  private String moduleCode;

  @TableId(value = "invoke_code")
  private String invokeCode;

  private String invokeDesc;

  private String invokeUrl;

  private Integer status;

  private Date createTime;

  private Integer isAuthUrl;//需要token认证的url：1-是；0-否


}
