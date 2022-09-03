package com.alan10607.leaf;

import com.alan10607.leaf.service.ViewService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest


class LeafApplicationTests {
	@Autowired
	private ViewService viewService;
	@Test
	void contextLoads() {
		viewService.findCountFromRedis("leaf");
	}

}
