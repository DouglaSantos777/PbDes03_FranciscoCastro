# Microsserviço de Gerenciamento de Ingressos (ms-ticket-manager)

Esse microsserviço é responsável pela criação, consulta, cancelamento e verificação de ingressos. Ele interage com o microsserviço de eventos para garantir que os ingressos estejam associados a eventos válidos. O serviço também permite o cancelamento de ingressos, com a opção de realizar soft delete, e realiza a validação de dados antes de criar ingressos.

## Ferramentas
- Java 17 (LTS)
- Spring Boot 3.3.7 (LTS)
- MongoDB Atlas
- RabbitMQ (para envio de e-mails de confirmação)
- Swagger (para documentação da API)

## Operações do Ms-Ticket-Manager

| Operação   | Método | Path                            | Regra                                                      |
|------------|--------|---------------------------------|------------------------------------------------------------|
| Criar      | POST   | `/create-ticket`                | Cria um ingresso para um evento existente, validando a existência do evento no ms-event-manager e enviando confirmação por e-mail via RabbitMQ. |
| Consultar  | GET    | `/get-ticket/{id}`              | Busca um ingresso pelo ID                                   |
| Consultar  | GET    | `/get-ticket-by-cpf/{cpf}`      | Busca ingressos pelo CPF                                    |
| Atualizar  | PUT    | `/update-ticket/{id}`           | Atualiza Tickets pelo id                                    |
| Cancelar   | DELETE | `/cancel-ticket/{id}`           | Cancela um ingresso pelo ID (soft-delete)                   |
| Cancelar   | DELETE | `/cancel-ticket/{cpf}`          | Cancela ingressos pelo CPF (soft-delete)                    |
| Consultar  | GET    | `/check-tickets-by-event/{eventId}` | Verifica ingressos vinculados a um evento                   |

## Cobertura de Testes com Jacoco

![image](https://github.com/user-attachments/assets/f5be1f64-c259-47cc-9121-061da580272e)

