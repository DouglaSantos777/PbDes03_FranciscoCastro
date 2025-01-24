package com.desafio03.ms_event.clientviacep;

public record Adress(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {
}
