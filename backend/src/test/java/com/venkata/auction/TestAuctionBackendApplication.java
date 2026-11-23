package com.venkata.auction;

import org.springframework.boot.SpringApplication;

public class TestAuctionBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuctionBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
