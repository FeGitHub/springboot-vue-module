package com.company.project.vo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class SysUserVo {


    private String id;

    @NotBlank(message = "账号不能为空")
    @Length(max = 32,min = 0,message = "姓名字段长度不能超过15")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(max = 32,min = 0,message = "密码字段长度不能超过15")
    private String password;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return USERNAME
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
