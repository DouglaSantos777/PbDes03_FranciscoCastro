package com.desafio03.ms_ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTicketApplication.class, args);
	}

}
