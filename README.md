# Delivery Tech API
Sistema de delivery robusto desenvolvido com Spring Boot 3.5 e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS**
- **Spring Boot 3.5.x**
- **Spring Security (JWT)**
- **Springdoc OpenAPI (Swagger)**
- **Spring Data JPA**
- **H2 Database**

## 📖 Documentação da API (Swagger)
A documentação interativa da API está disponível via Swagger UI. Através dela, é possível visualizar todos os endpoints, modelos de dados e testar as requisições em tempo real.

- **URL do Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **JSON OpenAPI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

### Autenticação na Interface
A API utiliza autenticação via Token JWT. Para testar endpoints protegidos no Swagger:
1. Acesse o endpoint `/api/auth/login` e realize o login para obter o token.
2. Clique no botão **"Authorize"** no topo da página do Swagger.
3. Insira o token no formato: `Bearer <seu_token>`.

## 📋 Endpoints Documentados

### Autenticação
- `POST /api/auth/cadastrar` - Registro de novo usuário.
- `POST /api/auth/login` - Autenticação e geração de token.
- `GET /api/auth/me` - Dados do usuário autenticado.

### Restaurantes
- `POST /restaurantes` - Cadastrar novo restaurante (Admin).
- `GET /restaurantes/listar` - Listar restaurantes ativos (Paginado).
- `GET /restaurantes/{id}` - Buscar restaurante por ID.
- `GET /restaurantes/categoria` - Buscar por categoria.
- `PATCH /restaurantes/{id}/toggle` - Ativar/Desativar restaurante.

### Produtos (Cardápio)
- `POST /produtos/restaurante/{id}` - Adicionar produto ao cardápio.
- `GET /produtos/restaurante/{id}` - Listar produtos de um restaurante.
- `PATCH /produtos/{id}/disponibilidade` - Alternar disponibilidade.

### Pedidos
- `POST /pedidos` - Realizar um novo pedido.
- `GET /pedidos/cliente/{id}` - Histórico de pedidos do cliente.
- `PUT /pedidos/{id}/confirmar` - Confirmar recebimento pelo restaurante.
- `PATCH /pedidos/{id}/status/avancar` - Avançar fluxo do pedido.
- `PATCH /pedidos/{id}/cancelar` - Cancelar pedido.

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado.
2. Clone o repositório.
3. Execute: `./mvnw spring-boot:run`
4. Acesse o Swagger UI para explorar a API.

## 👨‍💻 Desenvolvedor
[Victor Roos] - [04018 - ARQUITETURA DE SISTEMA]
Desenvolvido com JDK 21 e Spring Boot 3.5.3