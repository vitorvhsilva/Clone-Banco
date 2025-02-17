# Projeto que faz a Simulação de um Banco 
## Arquitetura do Projeto
<div>
  <img src="https://github.com/vitorvhsilva/Clone-Banco/blob/main/assets/arquitetura_banco.png">
</div>

## Resumo do Projeto
Este projeto visa simular um banco fícticio e suas funcionalidades mais utilizadas. Ele é baseado em 3 serviços principais:
- Serviço de Usuários: Responsável por toda a criação e armazenamento de usuários, incluindo dados como agência, conta, segmento. Por meio desse serviço também é possível para o usuário fazer um pedido de cartão ou pagar suas faturas, além de responder pedidos de pix feito pelo serviço de pagamentos.
- Serviço de Cartões: Responsável por pela criação e armazenamento dos cartões dos usuários e faturas, além de mostrar o catálogo disponível de cartões dependendo do segmento do usuário. Ele responde pedidos de criação de cartões e transações de crédito.
- Serviço de Pagamentos: Ponto de entrada para fazer pedidos de pix e crédito, armazenando todos os dados das transações que aconteceram.

Além disso, existe o serviço de API Gateway e Service Discovery, que agem em conjunto para criar um ponto de centralização de envio de requisição para os serviços, sendo possível criar uma arquitetura escalável e resiliente.

## Como rodar a Aplicação


## Tecnologias e Dependências Utilizadas
- Kotlin
- Java
- Spring Framework (Web, MVC, JPA, Validation, Feign, Eureka Server, Eureka Client, Gateway)
- Kafka
- Docker
- PostgreSQL
- MongoDB
- Slf4j
- Swagger
- AWS (S3)
