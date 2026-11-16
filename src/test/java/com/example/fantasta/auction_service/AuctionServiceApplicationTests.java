package com.example.fantasta.auction_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:service-test",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "auth.service.url=http://localhost:7070",
        "player.service.url=http://localhost:8080"
})
class AuctionServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
