# Microsserviço de Gerenciamento de Eventos (ms-event-manager)

Esse microsserviço é respnsável pela criação, consulta, atualização, listagem e exclusão de eventos. Ele interage com o microsserviço de tickets para validar se os ingressos vendidos estão associados a eventos existentes. O serviço também gerencia o status de eventos e permite a exclusão apenas se não houver ingressos vendidos.

## Ferramentas
Java 17 (LTS)
Spring Boot 3.3.7 (LTS)
MongoDB Atlas
Swagger (para documentação da API)
JUnit (Testes unitários)

## Operações do Ms-Events

| Operação   | Método | Path                        | Regra                                               |
|------------|--------|-----------------------------|-----------------------------------------------------|
| Criar      | POST   | `/create-event`             | Cria um evento, buscando o endereço a partir do CEP usando o ViaCEP |
| Consultar  | GET    | `/get-event/{id}`           | Busca um evento pelo ID                            |
| Consultar  | GET    | `/get-all-events`           | Lista todos os eventos                             |
| Consultar  | GET    | `/get-all-events/sorted`    | Lista eventos em ordem alfabética                   |
| Atualizar  | PUT    | `/update-event/{id}`        | Atualiza um evento pelo ID                         |
| Excluir    | DELETE | `/delete-event/{id}`        | Exclui um evento pelo ID (exclusão permitida apenas se não houver ingressos vendidos) |
 
