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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ViewServiceImpl implements ViewService {
    private final RedisTemplate redisTemplate;
    private final RedissonClient redisson;
    private final RedisKeyUtil redisKeyUtil;
    private final LeafService leafService;
    private final LeafCountDAO leafCountDAO;
    private final static int EXPIRE_TIME = 60;


    public LeafDTO findCountFromRedis(LeafDTO leafDTO) throws Exception {
        String leafName = leafDTO.getLeafName();
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");

        //先檢查是否存在redis
        String hashKey = redisKeyUtil.getLeafKey(leafName);
        if(!redisTemplate.hasKey(hashKey)){
            if(!findCountFromDB(leafName))//沒有就去DB查
                throw new IllegalStateException("findCountFromDB is busy, try later");
        }

        //已確定存在, 查詢redis
        RReadWriteLock lock = redisson.getReadWriteLock(redisKeyUtil.lock(leafName));
        Map<String, Number> map = new HashMap<>();//設置為Number, 因為超過2^31-1時會是Long, 否則會是Integer
        try {
            lock.readLock().lock();
            map = redisTemplate.opsForHash().entries(hashKey);
        }catch (Exception e){
            log.error("", e);
        }finally {
            lock.readLock().unlock();
        }

        leafDTO.setGood(map.get("good").longValue());
        leafDTO.setBad(map.get("bad").longValue());
        return leafDTO;
    }

    public void countIncr(LeafDTO leafDTO) throws Exception {
        String leafName = leafDTO.getLeafName();
        if(Strings.isBlank(leafName)) throw new IllegalStateException("LeafName can't be blank");

        String field = getCountType(leafDTO.getVoteFor());
        if(field.isEmpty()) throw new IllegalStateException("VoteFor is not defined");

        //先檢查是否存在redis
        String hashKey = redisKeyUtil.getLeafKey(leafName);
        if(!redisTemplate.hasKey(hashKey)){
            if(!findCountFromDB(leafName))//沒有就去DB查
                throw new IllegalStateException("findCountFromDB is busy, try later");
        }

        RReadWriteLock lock = redisson.getReadWriteLock(redisKeyUtil.lock(leafName));
        try {
            lock.writeLock().lock();
            redisTemplate.opsForHash().increment(hashKey, field, 1);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 到DB查詢後放入Redis, 採用分布式鎖防止失效時惡意重複查詢, 若鎖獲取失敗回傳false, 否則true
     * @param leafName
     * @return
     * @throws Exception
     */
    public boolean findCountFromDB(String leafName) throws Exception {
        RLock lock = redisson.getLock(redisKeyUtil.systemLock("findCountFromDB"));
        Boolean tryLock = false;
        try{
            tryLock = lock.tryLock();
            if(tryLock){
                //Cache Penetration, 防止緩存穿透, 查不到先設為0
                Optional<Leaf> leaf = leafCountDAO.findByLeafName(leafName);
                Map<String, Long> fields = leaf
                        .map(l -> Map.of("good", l.getGood(), "bad", l.getBad()))
                        .orElse(Map.of("good", 0L, "bad", 0L));

                if(leaf.isEmpty())
                    log.error("Find leaf count from DB but leafName:{} was not found", leafName);

                //Cache Avalanche, 防止緩存雪崩, 設定亂數過期時間
                String hashKey = redisKeyUtil.getLeafKey(leafName);
                redisTemplate.opsForHash().putAll(hashKey, fields);
                redisTemplate.expire(hashKey,redisKeyUtil.getExpireTime(EXPIRE_TIME), TimeUnit.SECONDS);
                log.info("FindCountFromDB for leafName:{} succeeded", leafName);
            }else{
                //Hotspot Invalid, 防止緩存擊穿, 若已經有查詢, 就先等一下再去Redis看
                Thread.sleep(1000);
                log.error("FindCountFromDB for leafName:{} was started, so sleep for 1 sec", leafName);
            }
        } catch (Exception e) {
            log.error("Find leaf count from DB failed", e);
            throw new Exception(e);
        } finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()){
                lock.unlock();//已鎖定且為當前線程的鎖, 才解鎖
            }
        }
        return tryLock;
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

/* 還是會有漏網之魚？
    RLock lock = redisson.getLock("testlock");
        log.error("before islocked:" + lock.isLocked());
        try {//最多等待3秒，上锁以后10秒自动解锁
//            boolean trylock = lock.tryLock(3, 10, TimeUnit.SECONDS);
        boolean trylock = lock.tryLock();
        log.error("after trylock:" + trylock + " islocked : " + lock.isLocked());
        if(trylock){
            Thread.sleep(10000);
        }else{
            log.error("NOT GET LOCK !! islocked:" + lock.isLocked());
        }
    }catch (Exception e){
        log.error("", e);
    }finally {
        if(lock.isLocked() && lock.isHeldByCurrentThread()){
            log.info("do unlock!");
            lock.unlock();
        }
    }
        log.info("FINISH");
*/
}