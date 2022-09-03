package com.alan10607.leaf.config;

import com.alan10607.leaf.dto.ViewDTO;
import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

//    @Bean
//    public LettuceConnectionFactory connectionFactory(){
//        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
//        conf.setHostName("localhost");
//        conf.setPort(6379);
//        return new LettuceConnectionFactory(conf);
//    }


//
//    public RedisConfig(
//            @Value("${redis.hostname}") String hostname,
//            @Value("${redis.port}") int port,
//            @Value("${redis.database}") int database,
//            @Value("${redis.password}") String password,
//            @Value("${redis.timeout}") long timeout
//    ) {
//
//        this.HOSTNAME = hostname;
//        this.PORT = port;
//        this.DATABASE = database;
//        this.PASSWORD = password;
//        this.TIMEOUT = timeout;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//        config.setHostName(HOSTNAME);
//        config.setPort(PORT);
//        config.setDatabase(DATABASE);
//        config.setPassword(PASSWORD);
//
//        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .commandTimeout(Duration.ofMillis(TIMEOUT))
//                .build();
//
//        return new LettuceConnectionFactory(config, clientConfig);
//    }

    @Bean
    public RedisTemplate<String, Long> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Long> template = new RedisTemplate<>();

        //設定連線工廠 改由application控制
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
//        template.setHashKeySerializer(new Jackson2JsonRedisSerializer(Long.class));
//        template.setHashValueSerializer(new Jackson2JsonRedisSerializer(Long.class));
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //啟用Transaction, 預設為禁用
        //template.setEnableTransactionSupport(true);

        //設定這些參數
        template.afterPropertiesSet();
        return template;
    }


}