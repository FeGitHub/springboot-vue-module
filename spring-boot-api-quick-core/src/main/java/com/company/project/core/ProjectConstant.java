package com.company.project.core;

/**
 * 项目常量
 */
public final class ProjectConstant {

    public static final String BASE_PACKAGE = "com.company.project";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）
    public static final String BASE_DB = ".slave";//如果是多数据源的就要看生成的是哪个
    public static final String MODEL_PACKAGE = BASE_PACKAGE + BASE_DB + ".model";//生成的Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + BASE_DB + ".dao";//生成的Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//生成的Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";//生成的Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名


    //数据库的相关配置(这个是用来生成代码的数据库配置，和项目运行无关)
    public static final String JDBC_URL = "jdbc:mysql://192.168.61.131:3306/slave?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    public static final String JDBC_USERNAME = "root";
    public static final String JDBC_PASSWORD = "root";
    public static final String JDBC_DIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

}
