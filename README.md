# Leaf
##### 這是一個類似popcat的點讚網頁, 對於一件事物人人都有不同喜好
##### 你喜歡這個就點讚, 不喜歡就點爛, 每個人都有無限多讚或爛可以點, 看誰比較會點
> 這是一個我用來學習測試spring boot, redis的專案
> 投票系統串連redis支援高併發, 並透過鎖機制增加效能, redis資料透過批次定時寫入db
> 順便引入spring security做一個管理系統

<br>

##### Redis鎖配置
![image](https://raw.githubusercontent.com/alan10607/Leaf/leaf-readme/redis-flow.jpg)

使用依賴: thymeleaf, spring security, lettuce, redisson, quartz, ...


```sh
% ab -n 2000 -c 100 -p post -T application/json http://localhost:8080/view/test
```

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
Server Port:            8080

Document Path:          /view/test
Document Length:        108 bytes

Concurrency Level:      100
Time taken for tests:   17.744 seconds
Complete requests:      2000
Failed requests:        1589
(Connect: 0, Receive: 0, Length: 1589, Exceptions: 0)
Total transferred:      795937 bytes
Total body sent:        356000
HTML transferred:       221937 bytes
Requests per second:    112.72 [#/sec] (mean)
Time per request:       887.188 [ms] (mean)
Time per request:       8.872 [ms] (mean, across all concurrent requests)
Transfer rate:          43.81 [Kbytes/sec] received
19.59 kb/s sent
63.40 kb/s total

Connection Times (ms)
min  mean[+/-sd] median   max
Connect:        0    0   0.4      0       3
Processing:    11  845 1889.2    165   16862
Waiting:       11  835 1889.5    163   16862
Total:         11  845 1889.3    166   16864

Percentage of the requests served within a certain time (ms)
50%    166
66%    422
75%    649
80%    911
90%   1922
95%   4576
98%   8023
99%  10398
100%  16864 (longest request)




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