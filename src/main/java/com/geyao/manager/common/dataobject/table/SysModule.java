package com.geyao.manager.common.dataobject.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_sys_module")
public class SysModule  implements Serializable {
  @TableId(value = "module_code")
  private String moduleCode;

  private String moduleName;

  private Integer status;

  private Date createTime;

  private String baseUrl;



}
