package com.alan10607.leaf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
@EnableRedisRepositories
public class RedisConfig {

//    @Bean
//    public LettuceConnectionFactory connectionFactory(){
//        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
//        conf.setHostName("localhost");
//        conf.setPort(6379);
//        return new LettuceConnectionFactory(conf);
//    }

    /**
     * 設定redis連線實例, Spring Boots預設用Lettuce
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Long> redisTemplate(LettuceConnectionFactory connectionFactory){
        log.info("RedisTemplate config HostName={}, Port={}",
                connectionFactory.getHostName(),
                connectionFactory.getPort());

        RedisTemplate<String, Long> template = new RedisTemplate<>();

        //設定連線工廠, LettuceConnectionFactory設定在application.properties
        template.setConnectionFactory(connectionFactory);

        /*
        StringRedisSerializer: 一般字串的序列化
        JdkSerializationRedisSerializer: 預設使用, 被反序列化的物件需implements Serializable
        Jackson2JsonRedisSerializer: json格式儲存, 初始化時指定反序列化class
        GenericJackson2JsonRedisSerializer: json格式儲存, 存入redis時會多存一個@class hashmap做為反序列化的class, 效率較低
         */
        //設定序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //啟用Transaction, 預設為禁用
        //template.setEnableTransactionSupport(true);

        //設定這些參數
        template.afterPropertiesSet();

        log.info("RedisTemplate config succeeded");
        return template;
    }

}