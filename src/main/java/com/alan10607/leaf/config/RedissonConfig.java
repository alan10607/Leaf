package com.alan10607.leaf.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class RedissonConfig {
    public String address;
    public String password;

    public RedissonConfig(@Value("${spring.redis.host}") String hostname,
            @Value("${spring.redis.port}") String port,
            @Value("${spring.redis.password}") String password) {
        this.address = String.format("redis://%s:%s", hostname, port);
        this.password = password;
    }
    /**
     * Build Redisson Client by redisson-config.yaml
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress(address)
                .setPassword(password);
        return Redisson.create(config);
    }
}