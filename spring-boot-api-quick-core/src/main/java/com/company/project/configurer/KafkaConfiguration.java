package com.company.project.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConfiguration {


    @Autowired
    private ConsumerFactory consumerFactory;


    // 监听器容器工厂(设置禁止KafkaListener自启动)
    @Bean
    public ConcurrentKafkaListenerContainerFactory delayContainerFactorys() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(consumerFactory);
        //禁止KafkaListener自启动
        container.setAutoStartup(false);
        return container;
    }
}
