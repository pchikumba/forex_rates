package com.wiremit.forex_rates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForexRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForexRatesApplication.class, args);
	}

}
