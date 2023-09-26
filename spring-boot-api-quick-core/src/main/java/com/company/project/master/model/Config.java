package com.company.project.master.model;

import javax.persistence.*;

public class Config {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 配置类型
     */
    @Column(name = "configType")
    private String configtype;

    /**
     * 配置类型说明
     */
    @Column(name = "configTypeName")
    private String configtypename;

    /**
     * 配置说明1
     */
    @Column(name = "propName1")
    private String propname1;

    /**
     * 配置数据1
     */
    @Column(name = "propVal1")
    private String propval1;

    /**
     * 配置说明2
     */
    @Column(name = "propName2")
    private String propname2;

    /**
     * 配置数据2
     */
    @Column(name = "propVal2")
    private String propval2;

    /**
     * 配置说明3
     */
    @Column(name = "propName3")
    private String propname3;

    /**
     * 配置数据3
     */
    @Column(name = "propVal3")
    private String propval3;

    /**
     * 配置说明4
     */
    @Column(name = "propName4")
    private String propname4;

    /**
     * 配置数据4
     */
    @Column(name = "propVal4")
    private String propval4;

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
     * 获取配置类型
     *
     * @return configType - 配置类型
     */
    public String getConfigtype() {
        return configtype;
    }

    /**
     * 设置配置类型
     *
     * @param configtype 配置类型
     */
    public void setConfigtype(String configtype) {
        this.configtype = configtype;
    }

    /**
     * 获取配置类型说明
     *
     * @return configTypeName - 配置类型说明
     */
    public String getConfigtypename() {
        return configtypename;
    }

    /**
     * 设置配置类型说明
     *
     * @param configtypename 配置类型说明
     */
    public void setConfigtypename(String configtypename) {
        this.configtypename = configtypename;
    }

    /**
     * 获取配置说明1
     *
     * @return propName1 - 配置说明1
     */
    public String getPropname1() {
        return propname1;
    }

    /**
     * 设置配置说明1
     *
     * @param propname1 配置说明1
     */
    public void setPropname1(String propname1) {
        this.propname1 = propname1;
    }

    /**
     * 获取配置数据1
     *
     * @return propVal1 - 配置数据1
     */
    public String getPropval1() {
        return propval1;
    }

    /**
     * 设置配置数据1
     *
     * @param propval1 配置数据1
     */
    public void setPropval1(String propval1) {
        this.propval1 = propval1;
    }

    /**
     * 获取配置说明2
     *
     * @return propName2 - 配置说明2
     */
    public String getPropname2() {
        return propname2;
    }

    /**
     * 设置配置说明2
     *
     * @param propname2 配置说明2
     */
    public void setPropname2(String propname2) {
        this.propname2 = propname2;
    }

    /**
     * 获取配置数据2
     *
     * @return propVal2 - 配置数据2
     */
    public String getPropval2() {
        return propval2;
    }

    /**
     * 设置配置数据2
     *
     * @param propval2 配置数据2
     */
    public void setPropval2(String propval2) {
        this.propval2 = propval2;
    }

    /**
     * 获取配置说明3
     *
     * @return propName3 - 配置说明3
     */
    public String getPropname3() {
        return propname3;
    }

    /**
     * 设置配置说明3
     *
     * @param propname3 配置说明3
     */
    public void setPropname3(String propname3) {
        this.propname3 = propname3;
    }

    /**
     * 获取配置数据3
     *
     * @return propVal3 - 配置数据3
     */
    public String getPropval3() {
        return propval3;
    }

    /**
     * 设置配置数据3
     *
     * @param propval3 配置数据3
     */
    public void setPropval3(String propval3) {
        this.propval3 = propval3;
    }

    /**
     * 获取配置说明4
     *
     * @return propName4 - 配置说明4
     */
    public String getPropname4() {
        return propname4;
    }

    /**
     * 设置配置说明4
     *
     * @param propname4 配置说明4
     */
    public void setPropname4(String propname4) {
        this.propname4 = propname4;
    }

    /**
     * 获取配置数据4
     *
     * @return propVal4 - 配置数据4
     */
    public String getPropval4() {
        return propval4;
    }

    /**
     * 设置配置数据4
     *
     * @param propval4 配置数据4
     */
    public void setPropval4(String propval4) {
        this.propval4 = propval4;
    }
}