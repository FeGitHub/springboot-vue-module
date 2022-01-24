package com.company.project.configurer;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;

import java.util.Properties;

import static com.company.project.core.ProjectConstant.BASE_PACKAGE;

/**
 * Mybatis & Mapper & PageHelper 配置
 */

@Configuration
public class SlaveDataSourceConfig {
    public static final String SLAVE_DB =  ".slave";
    public static final String SLAVE_MAPPER_PACKAGE = BASE_PACKAGE +SLAVE_DB+ ".dao";
    public static final String SLAVE_MODEL_PACKAGE = BASE_PACKAGE +SLAVE_DB+ ".model";


    @Bean(name = "slaveDataSource")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().build();
    }



    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("slaveDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTypeAliasesPackage(SLAVE_MODEL_PACKAGE);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources("classpath:mapper/slave/*.xml"));
        return factory.getObject();
    }


    @Bean(name = "slaveMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("slaveSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(SLAVE_MAPPER_PACKAGE);
        //配置通用Mapper，详情请查阅官方文档
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.company.project.core.Mapper");
        properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        //properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);

        return mapperScannerConfigurer;
    }



}

