# Leaf
<p align="center">
	<a href="https://github.com/alan10607/Leaf" target="_blank">
        <img src="https://img.shields.io/badge/Github-alan10607/Leaf-green">
	</a>
	<a href="https://hub.docker.com/r/alan10607/leaf" target="_blank">
        <img src="https://img.shields.io/badge/Docker-alan10607/Leaf-blue">
	</a>
    <img src="https://img.shields.io/badge/JDK-11-lightgray">
    <img src="https://img.shields.io/badge/Spring Boot-v2.7.3-lightgray">
    <img src="https://img.shields.io/badge/Redis-v7.0.4-lightgray">
    <img src="https://img.shields.io/badge/MySql-v8.0.30-lightgray">
</p>

___English readme down below___

> 這是我用來學習測試高併發的Web專案, 使用Spring Boot, Redis, Docker  
> 類似popcat點讚, 但是主題是「香菜」, 喜歡香菜可以按讚, 不喜歡就按爛, 可以無限次點擊投票  
> 「香菜」這個投票主題被稱為Leaf, 可以建立無線多個Leaf做為投票主題

## 大綱
- 使用Docker-Compose啟動容器Spring Boots, Redis, MySql
- 實作Redis緩存擊穿、穿透、雪崩之解決方案, 並採用讀寫鎖, 分佈式鎖存取資料
- 透過定期任務排程將Redis緩存存入DB持久化
- 引入Spring Security建立管理員介面, 可對管理員、Leaf進行管理  

![image](https://raw.githubusercontent.com/alan10607/Leaf/leaf-readme/screen-shot.jpg)

## docker-compose.yml
```
version: "3.9"
services:
  leaf-server:
    image: alan10607/leaf
    container_name: leaf-server
    ports:
      - "8081:8080"
    volumes:
      - "~/docker/volume/leaf/log:/log"
    environment:
      - LOG_PATH=/log
      - MYSQL_HOST=leaf-mysql
      - MYSQL_PORT=3306
      - MYSQL_USER=root
      - MYSQL_PASSWORD=root
      - REDIS_HOST=leaf-redis
      - REDIS_PORT=6379
      - REDIS_PASSWORD=root
    depends_on:
      - leaf-mysql
      - leaf-redis
    networks:
      - proxy
    command: ["/wait-for-it.sh", "leaf-mysql:3306", "--", "/wait-for-it.sh", "leaf-redis:6379", "--", "java","-jar","/leaf-server.jar"]

  leaf-mysql:
    image: mysql:8.0.30
    container_name: leaf-mysql
    ports:
      - "3307:3306"
    environment:
      - MYSQL_DATABASE=leaf
      - MYSQL_ROOT_PASSWORD=root
    networks:
      - proxy

  leaf-redis:
    image: redis:7.0.4
    container_name: leaf-redis
    ports:
      - "6380:6379"
    volumes:
      - "~/docker/volume/redis/data:/data"
      - "~/docker/volume/redis/redis.conf:/usr/local/etc/redis/redis.conf"
      - "~/docker/volume/redis/logs:/logs"
    command: redis-server /usr/local/etc/redis/redis.conf --appendonly yes --requirepass "root"
    networks:
      - proxy

networks:
  proxy:
    name: leaf-network
```

## Redis鎖配置
![image](https://raw.githubusercontent.com/alan10607/Leaf/leaf-readme/redis-flow.jpg)

## 壓力測試
透過ApacheBench測試, 隨機分配請求: 10%票good (寫), 10%票bad (寫), 其餘80%為查詢 (讀)  
設定2000個request, 併發100, failed requests為可忽略之長度錯誤
```sh
$ ab -n 2000 -c 100 -p post -T application/json http://localhost:8081/view/test
```

```text
This is ApacheBench, Version 2.3 <$Revision: 1879490 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 200 requests
Completed 400 requests
Completed 600 requests
Completed 800 requests
Completed 1000 requests
Completed 1200 requests
Completed 1400 requests
Completed 1600 requests
Completed 1800 requests
Completed 2000 requests
Finished 2000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8081

Document Path:          /view/test
Document Length:        116 bytes

Concurrency Level:      100
Time taken for tests:   4.519 seconds
Complete requests:      2000
Failed requests:        1795
   (Connect: 0, Receive: 0, Length: 1795, Exceptions: 0)
Total transferred:      799444 bytes
Total body sent:        356000
HTML transferred:       225444 bytes
Requests per second:    442.55 [#/sec] (mean)
Time per request:       225.965 [ms] (mean)
Time per request:       2.260 [ms] (mean, across all concurrent requests)
Transfer rate:          172.75 [Kbytes/sec] received
                        76.93 kb/s sent
                        249.68 kb/s total

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.7      0       5
Processing:     5  213 595.8     18    4385
Waiting:        4  213 595.8     18    4384
Total:          5  214 595.9     18    4388

Percentage of the requests served within a certain time (ms)
  50%     18
  66%     35
  75%     51
  80%     73
  90%    676
  95%   1559
  98%   2302
  99%   3258
 100%   4388 (longest request)
```

###
Server log
```
...
2022-09-11 23:01:18.412 [exec-87] - findCountFromRedis: {good=15, bad=7}
2022-09-11 23:01:18.421 [exec-22] - findCountFromRedis: {good=15, bad=7}
2022-09-11 23:01:18.452 [exec-65] - countIncr: bad=8
2022-09-11 23:01:18.508 [exec-27] - countIncr: good=16
2022-09-11 23:01:18.567 [exec-84] - countIncr: bad=9
2022-09-11 23:01:18.620 [exec-82] - countIncr: good=17
2022-09-11 23:01:18.666 [exec-2] - countIncr: good=18
2022-09-11 23:01:18.697 [exec-89] - findCountFromRedis: {good=18, bad=9}
2022-09-11 23:01:18.699 [exec-102] - findCountFromRedis: {good=18, bad=9}
...
```

<hr>

## ___English Readme___

> This is the web project I used to learn high concurrency solutions, build in Spring Boot, Redis, Docker.  
> Like "POPCAT", but the theme is "cilantro". If you like cilantro, you click like. But if you don't like it, you can click bad. Just like POPCAT, you can click and vote unlimited times.  
> The voting theme of "cilantro" is called Leaf, and multiple Leaves can be created as the voting theme.
## Features
- Docker-compose to run Spring Boots, Redis, MySql
- Solutions for Redis cache issues: Hotspot Invalid, Cache Penetration, and Cache Avalanche
- Use read-write locks and distributed locks to access data
- Task scheduling save cache to database
- Depend on Spring Security to integrate admin interface, which manage users and Leaf  

![image](https://raw.githubusercontent.com/alan10607/Leaf/leaf-readme/screen-shot.jpg)

## docker-compose.yml
```
version: "3.9"
services:
  leaf-server:
    image: alan10607/leaf
    container_name: leaf-server
    ports:
      - "8081:8080"
    volumes:
      - "~/docker/volume/leaf/log:/log"
    environment:
      - LOG_PATH=/log
      - MYSQL_HOST=leaf-mysql
      - MYSQL_PORT=3306
      - MYSQL_USER=root
      - MYSQL_PASSWORD=root
      - REDIS_HOST=leaf-redis
      - REDIS_PORT=6379
      - REDIS_PASSWORD=root
    depends_on:
      - leaf-mysql
      - leaf-redis
    networks:
      - proxy
    command: ["/wait-for-it.sh", "leaf-mysql:3306", "--", "/wait-for-it.sh", "leaf-redis:6379", "--", "java","-jar","/leaf-server.jar"]

  leaf-mysql:
    image: mysql:8.0.30
    container_name: leaf-mysql
    ports:
      - "3307:3306"
    environment:
      - MYSQL_DATABASE=leaf
      - MYSQL_ROOT_PASSWORD=root
    networks:
      - proxy

  leaf-redis:
    image: redis:7.0.4
    container_name: leaf-redis
    ports:
      - "6380:6379"
    volumes:
      - "~/docker/volume/redis/data:/data"
      - "~/docker/volume/redis/redis.conf:/usr/local/etc/redis/redis.conf"
      - "~/docker/volume/redis/logs:/logs"
    command: redis-server /usr/local/etc/redis/redis.conf --appendonly yes --requirepass "root"
    networks:
      - proxy

networks:
  proxy:
    name: leaf-network
```

## Redis locks
![image](https://raw.githubusercontent.com/alan10607/Leaf/leaf-readme/redis-flow.jpg)

## Stress Testing
Through the ApacheBench test, randomly assigns requests for 10% good ballot (write), 10% bad ballot (write), and 80% queries (read)  
Set 2000 requests, 100 concurrent, failed requests are ignorable length failures

```sh
$ ab -n 2000 -c 100 -p post -T application/json http://localhost:8081/view/test
```

```text
This is ApacheBench, Version 2.3 <$Revision: 1879490 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 200 requests
Completed 400 requests
Completed 600 requests
Completed 800 requests
Completed 1000 requests
Completed 1200 requests
Completed 1400 requests
Completed 1600 requests
Completed 1800 requests
Completed 2000 requests
Finished 2000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8081

Document Path:          /view/test
Document Length:        116 bytes

Concurrency Level:      100
Time taken for tests:   4.519 seconds
Complete requests:      2000
Failed requests:        1795
   (Connect: 0, Receive: 0, Length: 1795, Exceptions: 0)
Total transferred:      799444 bytes
Total body sent:        356000
HTML transferred:       225444 bytes
Requests per second:    442.55 [#/sec] (mean)
Time per request:       225.965 [ms] (mean)
Time per request:       2.260 [ms] (mean, across all concurrent requests)
Transfer rate:          172.75 [Kbytes/sec] received
                        76.93 kb/s sent
                        249.68 kb/s total

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.7      0       5
Processing:     5  213 595.8     18    4385
Waiting:        4  213 595.8     18    4384
Total:          5  214 595.9     18    4388

Percentage of the requests served within a certain time (ms)
  50%     18
  66%     35
  75%     51
  80%     73
  90%    676
  95%   1559
  98%   2302
  99%   3258
 100%   4388 (longest request)
```

###
Server log
```
...
2022-09-11 23:01:18.412 [exec-87] - findCountFromRedis: {good=15, bad=7}
2022-09-11 23:01:18.421 [exec-22] - findCountFromRedis: {good=15, bad=7}
2022-09-11 23:01:18.452 [exec-65] - countIncr: bad=8
2022-09-11 23:01:18.508 [exec-27] - countIncr: good=16
2022-09-11 23:01:18.567 [exec-84] - countIncr: bad=9
2022-09-11 23:01:18.620 [exec-82] - countIncr: good=17
2022-09-11 23:01:18.666 [exec-2] - countIncr: good=18
2022-09-11 23:01:18.697 [exec-89] - findCountFromRedis: {good=18, bad=9}
2022-09-11 23:01:18.699 [exec-102] - findCountFromRedis: {good=18, bad=9}
...
```
