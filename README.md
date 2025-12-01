# API de Cadastro e Consulta de Veículos (FIPE API)

Este projeto consiste em uma aplicação Java desenvolvida para realizar o cadastro de veículos, integrar-se à API FIPE para consulta automática de valores e persistir os dados em um banco PostgreSQL.
O sistema inclui operações de criação, atualização e consulta, seguindo boas práticas de desenvolvimento orientado a objetos e arquitetura de APIs REST.

---

## Funcionalidades Implementadas

### 1. Cadastro de veículo

Permite inserir dados essenciais de um veículo, associando marca, modelo, ano, tipo de combustível e outros atributos relevantes.

### 2. Consulta automática de valor FIPE

Ao cadastrar um veículo, o sistema consulta a API FIPE com os parâmetros necessários (marca, modelo, tipo e ano) e deve armazenar o preço correspondente.

### 3. Atualização de informações

O método `PUT` realiza atualização parcial, preservando campos já existentes e substituindo apenas aqueles enviados na requisição.

### 4. Persistência em banco de dados

Utilização do PostgreSQL com Spring Data JPA, garantindo integridade e consistência dos dados.
Comandos `save()` e `saveAndFlush()` são usados conforme a necessidade de escrita imediata.

### 5. Tratamento de erros

Foram implementadas validações e mensagens claras para situações como ausência de retorno da FIPE, dados inconsistentes ou falhas de persistência.

---

## Funcionalidade Não Concluída: Mensageria

O projeto previa a implementação de um módulo de mensageria utilizando RabbitMQ ou tecnologia equivalente.
Entretanto, após estudo e tentativas iniciais, identifiquei que **não possuía ainda maturidade técnica suficiente** para elaborar essa parte com a qualidade e robustez necessárias.

Para não comprometer a estabilidade do projeto principal, a implementação da mensageria foi adiada e será desenvolvida futuramente, após aprofundamento teórico e prático nas tecnologias envolvidas.

---

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* IntelliJ IDEA
* API FIPE ((https://deividfortuna.github.io/fipe/))

---

## Como Executar o Projeto

### 1. Clonar o repositório

```
git clone https://github.com/seu-repositorio.git
```

### 2. Configurar o `application.properties`

```
spring.datasource.url=jdbc:postgresql://localhost:5432/nomedb
spring.datasource.username=postgres
spring.datasource.password=suasenha
spring.jpa.hibernate.ddl-auto=update
```

### 3. Executar via Maven

```
mvn spring-boot:run
```

Ou gerar o arquivo `.jar`:

```
mvn clean install
java -jar target/seu-projeto.jar
```

---

## Referências Utilizadas

### Vídeos

1. [https://youtu.be/yW7RrWfUeHE?si=AROiICVIvYlmevjY](https://youtu.be/yW7RrWfUeHE?si=AROiICVIvYlmevjY)
2. [https://youtu.be/T6ChO8LQxRE?si=NEevWsNLrPewFdCR](https://youtu.be/T6ChO8LQxRE?si=NEevWsNLrPewFdCR)
3. [https://youtu.be/LX5jaieOIAk?si=ufw8dTbQa1phNlYa](https://youtu.be/LX5jaieOIAk?si=ufw8dTbQa1phNlYa)
4. [https://youtu.be/rVSwDX9KUt8?si=94PV6Xf2jWLAbbJr](https://youtu.be/rVSwDX9KUt8?si=94PV6Xf2jWLAbbJr)
5. [https://youtu.be/Cmk0iOObSPs?si=KAtmr65lWMPzdPi-](https://youtu.be/Cmk0iOObSPs?si=KAtmr65lWMPzdPi-)
6. [https://youtu.be/ih2ybBhlEis?si=eBEt54Yg-dVDrbp](https://youtu.be/ih2ybBhlEis?si=eBEt54Yg-dVDrbp)_

### Cursos

* Cursos de Java iniciante e intermediário da plataforma Alura

### Apoio para dúvidas gerais

* ChatGPT, utilizado para esclarecimento de dúvidas relacionadas à IDE, erros de compilação, entendimento de fluxos, boas práticas e explicações conceituais.

---

## Status do Projeto

* Funcionalidades principais implementadas
* Integração com FIPE concluída
* Persistência estável em PostgreSQL
* Mensageria prevista, porém não implementada nesta versão
* Documentação em construção contínua
