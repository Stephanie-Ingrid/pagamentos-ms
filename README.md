#  API Pagamentos MS

Simula microserviço de transação de cartões, tem integração com
Eureka Sever do Spring Cloud Netflix, Circuit Breaker 
para caso de inatividade na API, openfeign para comunicação com microserviço Pedidos.

### Ferramentas para Executar

- Java 17
- maven
- Lombok
- Docker

## Para rodar o projeto 
Antes de tudo suba o banco, o arquivo está na pasta database com nome `docker-compose.yaml`, 
suba a imagem com o comando a seguir:

    $ docker compose up

Quando a imagem do docker estiver rodando, é só fazer o build da aplicação que vai rodar corretamente.

_Essa aplicação foi desenvolvida para fins de estudo de load balancer, gateway e discovery service._