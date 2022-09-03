package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.dto.LeafDTO;
import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.RedisKeyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ViewServiceImpl implements ViewService {
    private final RedisTemplate redisTemplate;
    private final RedissonClient redisson;
    private final RedisKeyUtil redisKeyUtil;

    public LeafDTO findCountFromRedis(String leafName) {
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");

        RReadWriteLock lock = redisson.getReadWriteLock(redisKeyUtil.lock(leafName));
        LeafDTO leafDTO = new LeafDTO();
        try {
            lock.readLock().lock();

            String hashKey = redisKeyUtil.getLeafKey(leafName);
            Map<String, Object> map = redisTemplate.opsForHash().entries(hashKey);
            if(map.isEmpty()){
                // findCountFromDB(leafName);//重新去DB抓
                return null;
            }else{
                leafDTO.setGood(toLong(map.get("good")));//超過2^31-1時會是Long, 否則會是Integer
                leafDTO.setBad(toLong(map.get("bad")));
                log.info("[REDIS] findCountFromRedis by " + Thread.currentThread().getName());
            }
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.readLock().unlock();
        }
        return leafDTO;
    }

    public long toLong(Object integerOrLong){
        return integerOrLong instanceof Integer ? (Integer) integerOrLong : (Long) integerOrLong;
    }

/*




    private final LeafService leafService;

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


    public void findCountFromDB(String leafName) {
        RLock lock = redisson.getLock(keyUtil.getLeafCountDB());
        if(lock.isLocked()) return;

        try{
            lock.lock();

            System.out.println("DB called findCountFromDB " + Thread.currentThread().getName());
            LeafDTO leafDTO = leafService.findCount(leafName);//有可能查不到

            String hashKey = keyUtil.getLeafKey(leafName);
            redisTemplate.opsForHash().putAll(hashKey, Map.of("count1", leafDTO.getGood(), "count2", leafDTO.getBad()));
            redisTemplate.expire(hashKey,3600, TimeUnit.SECONDS);//expire in 1 hour
            redisTemplate.opsForList().rightPush(keyUtil.schQueue(), hashKey);


            System.out.println("DB called findCountFromDB OK " + Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("get leaf count from db failed", e);
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
        return leafDTO;
    }

    public long countIncr(String leafName, int voteFor) {
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");
        String field = getCountType(voteFor);
        if(field.isEmpty()) throw new IllegalStateException("VoteFor is not defined");

        RReadWriteLock lock = redisson.getReadWriteLock(RedisKeyUtil.lock(leafName));
        long res = -1L;
        try {
            lock.writeLock().lock();
            String hashKey = keyUtil.getLeafKey(leafName);
            if(!redisTemplate.hasKey(hashKey))
                findCountFromDB(hashKey);

            redisTemplate.opsForHash().increment(hashKey, field, 1);

            System.out.println("REDIS called countIncr " + Thread.currentThread().getName());
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.writeLock().unlock();
        }
        return res;
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

    private String getCountType(int voteFor) {
        for (CountType type : CountType.values()) {
            if(type.getVoteFor() == voteFor)
                return type.getField();
        }
        return "";
    }
*/

}