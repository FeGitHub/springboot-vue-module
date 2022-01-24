package com.company.project.configurer;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.company.project.core.Mapper;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

import static com.company.project.core.ProjectConstant.*;

/**
 * Mybatis & Mapper & PageHelper 配置
 */
@Configuration
public class MasterDataSourceConfig {
    public static final String MASTER_DB =  ".master";
    public static final String MASTER_MAPPER_PACKAGE = BASE_PACKAGE +MASTER_DB+ ".dao";
    public static final String MASTER_MODEL_PACKAGE = BASE_PACKAGE +MASTER_DB+ ".model";

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }


    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("masterDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTypeAliasesPackage(MASTER_MODEL_PACKAGE);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources("classpath:mapper/master/*.xml"));
        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.registerMapper(Mapper.class);
        return factory.getObject();
    }


    @Primary
    @Bean(name = "masterMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("masterSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(MASTER_MAPPER_PACKAGE);

        //配置通用Mapper，详情请查阅官方文档
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.company.project.core.Mapper");
        properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        //properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }



}

