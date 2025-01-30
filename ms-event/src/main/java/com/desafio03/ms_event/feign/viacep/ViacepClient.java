package com.desafio03.ms_event.feign.viacep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br/ws/", name = "viacep")
public interface ViacepClient {

    @GetMapping("{cep}/json/")
    Adress getAdress(@PathVariable String cep);
}
