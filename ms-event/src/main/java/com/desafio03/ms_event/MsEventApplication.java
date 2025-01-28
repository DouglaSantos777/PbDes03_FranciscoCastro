package com.desafio03.ms_event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
public class MsEventApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEventApplication.class, args);
	}

}
