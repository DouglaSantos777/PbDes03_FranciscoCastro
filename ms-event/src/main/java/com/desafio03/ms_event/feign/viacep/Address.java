package com.desafio03.ms_event.feign.viacep;

public record Address(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {
}
