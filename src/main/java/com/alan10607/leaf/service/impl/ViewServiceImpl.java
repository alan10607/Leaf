package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.constant.CountType;
import com.alan10607.leaf.dao.LeafCountDAO;
import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.model.Leaf;
import com.alan10607.leaf.service.LeafService;
import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.RedisKeyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ViewServiceImpl implements ViewService {
    private final RedisTemplate redisTemplate;
    private final RedissonClient redisson;
    private final RedisKeyUtil redisKeyUtil;
    private final LeafService leafService;

    public LeafDTO findCountFromRedis(String leafName) {
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");

        RReadWriteLock lock = redisson.getReadWriteLock(redisKeyUtil.lock(leafName));
        LeafDTO leafDTO = new LeafDTO();
        try {
            lock.readLock().lock();

            String hashKey = redisKeyUtil.getLeafKey(leafName);
            Map<String, Number> map = redisTemplate.opsForHash().entries(hashKey);
            if(map.isEmpty())
                findCountFromDB(leafName);

            leafDTO.setGood(map.get("good").longValue());//超過2^31-1時會是Long, 否則會是Integer
            leafDTO.setBad(map.get("bad").longValue());
            log.info("[REDIS] findCountFromRedis " + map);
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.readLock().unlock();
        }
        return leafDTO;
    }

    public long countIncr(String leafName, int voteFor) {
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");
        String field = getCountType(voteFor);
        if(field.isEmpty()) throw new IllegalStateException("VoteFor is not defined");

        RReadWriteLock lock = redisson.getReadWriteLock(redisKeyUtil.lock(leafName));
        long res = -1L;
        try {
            lock.writeLock().lock();
            String hashKey = redisKeyUtil.getLeafKey(leafName);
            if(!redisTemplate.hasKey(hashKey)){}
                //findCountFromDB(hashKey);

            res = redisTemplate.opsForHash().increment(hashKey, field, 1);

            log.info("[REDIS] countIncr " + field + ":" + res);
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.writeLock().unlock();
        }
        return res;
    }
//TO-DO: fix
    public LeafDTO findCountFromDB(String leafName) throws Exception {
        String hashKey = redisKeyUtil.getLeafKey(leafName);
        RLock lock = redisson.getLock(redisKeyUtil.getLeafCountDB());

        //Hotspot Invalid, 防止緩存擊穿, 若已經有查詢, 就先等一下再去Redis看
        if(!lock.tryLock()) {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){}
            log.error("[Redis] findCountFromDB was started, sleep for 1 sec");
            if(redisTemplate.hasKey(hashKey)){
                findCountFromDB(leafName);
            }
            return false;
        }

        try{
            lock.lock();
            Map<String, Long> fields = new HashMap<>();
            try{
                LeafDTO leafDTO = leafService.findCount(leafName);//有可能查不到
                fields = Map.of("good", leafDTO.getGood(), "bad", leafDTO.getBad());
            }catch (Exception e) {
                //Cache Penetration, 防止緩存穿透, 查不到就塞0
                fields = Map.of("good", 0L, "bad", 0L);
                log.error("[Redis] ", e);
            }

            redisTemplate.opsForHash().putAll(hashKey, fields);
            redisTemplate.expire(hashKey,redisKeyUtil.getExpireTime(3600), TimeUnit.SECONDS);//Cache Avalanche, 防止雪崩, 設定亂數過期時間
            redisTemplate.opsForList().rightPush(redisKeyUtil.schQueue(), hashKey);//放入批次佇列
            log.info("[Redis] findCountFromDB: " + leafName);
        } catch (Exception e) {
            log.error("[Redis] Get leaf count from db failed", e);
            throw new Exception(e);
        } finally {
            lock.unlock();
        }
        return true;
    }

/*





    public LeafDTO findCountFromRedis(String leafName) {
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");

        RReadWriteLock lock = redisson.getReadWriteLock(keyUtil.lock(leafName));
        LeafDTO leafDTO = new LeafDTO();
        try {
            lock.readLock().lock();
            String hashKey = keyUtil.getLeafKey(leafName);
            leafDTO =  (LeafDTO) redisTemplate.opsForHash().entries(hashKey);
            if(leafDTO == null)
                findCountFromDB(leafName);//重新去DB抓

            System.out.println("REDIS called get " + Thread.currentThread().getName());
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.readLock().unlock();
        }
        return leafDTO;
    }






    public void saveCountToDB() {
        RLock lock = redisson.getLock(keyUtil.saveLeafCountDB());//防止分佈式系統重跑才需要這個lock
        String leafName = "";
        if(lock.isLocked()) return;
        try {
            lock.lock();
            if(redisTemplate.opsForList().size(keyUtil.schQueue()) == 0)
                return;

            leafName = (String) redisTemplate.opsForList().leftPop(keyUtil.schQueue());

            System.out.println("REDIS saveLeafCountToDB leafName=" + leafName + Thread.currentThread().getName());

            RReadWriteLock rwLock = redisson.getReadWriteLock(keyUtil.lock(leafName));
            LeafDTO leafDTO = new LeafDTO();
            try {
                lock.readLock().lock();
                String hashKey = keyUtil.getLeafKey(leafName);
                leafDTO =  (LeafDTO) redisTemplate.opsForHash().entries(hashKey);
            }catch (Exception e){
                log.error("", e);
            }finally {
                rwLock.readLock().unlock();
            }

            if(leafDTO != null) {
                redisTemplate.opsForList().rightPush(keyUtil.saveLeafCountDB(), leafName);
                leafService.updateCount(leafDTO);
                System.out.println("REDIS saveLeafCountToDB OK!! leafName=" + leafName + Thread.currentThread().getName());
            }

            System.out.println("REDIS saveLeafCountToDB OK" + Thread.currentThread().getName());
        }catch (Exception e){
            log.error("", e);
            if(!leafName.isEmpty()){
                redisTemplate.opsForList().rightPush(keyUtil.saveLeafCountDB(), leafName);
                log.error("save leaf:" + leafName + " error, offer to queue!", e);
            }
        }finally {
            lock.unlock();
        }
    }


*/

    private String getCountType(int voteFor) {
        for (CountType type : CountType.values()) {
            if(type.getVoteFor() == voteFor)
                return type.getField();
        }
        return "";
    }

}