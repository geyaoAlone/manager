package com.geyao.manager.common.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("t_sys_user")
public class SysUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 模块编号
     */
    private String moduleCode;

    /**
     * 商户编号
     */
    private String merchantCode;
    /**
     * 用户名
     * 可以是手机号
     */
    private String username;
    /**
     * 密码
     * 可为空
     */
    private String password;
    /**
     * 用户角色：
     * 00 - 系统管理员
     * 01 - 小系统管理员
     * 02 - 客户
     */
    private String role;

    private String mobile;

    private int status;

    private Date createTime;

}
