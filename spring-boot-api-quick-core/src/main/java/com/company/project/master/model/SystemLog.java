package com.company.project.master.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "system_log")
public class SystemLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 类型
     */
    private String type;

    /**
     * 日志信息
     */
    private String message;

    /**
     * 创建日期
     */
    @Column(name = "creation_time")
    private Date creationTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取日志信息
     *
     * @return message - 日志信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置日志信息
     *
     * @param message 日志信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取创建日期
     *
     * @return creation_time - 创建日期
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * 设置创建日期
     *
     * @param creationTime 创建日期
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}