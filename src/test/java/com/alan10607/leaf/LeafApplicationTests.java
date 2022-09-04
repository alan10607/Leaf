package com.alan10607.leaf;

import com.alan10607.leaf.service.ViewService;
import com.mysql.cj.xdevapi.AbstractFilterParams;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest


class LeafApplicationTests {
	@Autowired
	private RedissonClient redisson;
	
	@Test
	void contextLoads() {
		RLock lock = redisson.getLock("testlock");
		try {
			lock.lock();
			Thread.sleep(1000);
		}catch (Exception e){

		}finally {
			lock.unlock();
			System.out.println("unlock, trylock:" + lock.tryLock());
		}


		if(lock.tryLock()){
			try {
				lock.lock();
				Thread.sleep(1000);
			}catch (Exception e){

			}finally {
				lock.unlock();
				System.out.println("unlock, trylock:" + lock.tryLock());
			}
		}else{
			System.out.println("already locked, trylock:" + lock.tryLock());
		}
	}

}
