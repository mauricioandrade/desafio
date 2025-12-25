# 🚀 Desafio Técnico – Mini Catálogo (Produto & Categoria)

Este projeto foi desenvolvido como parte do **Desafio Técnico para Desenvolvedor Java Júnior**, com o objetivo de avaliar conhecimentos fundamentais em **Spring Boot**, **REST APIs**, **JPA** e **modelagem de dados**.

---

## 🔗 Repositórios

- 📁 **Meu projeto (implementação):**  
  https://github.com/mauricioandrade/desafio

- 📄 **Desafio original:**  
  https://github.com/matheuslf/dev-matheuslf-desafio-modulo1

---

## 🧠 Visão Geral do Desafio

Criar uma **API REST simples** para gerenciar um **mini catálogo de Produtos e Categorias**, respeitando regras de modelagem, endpoints e padrões de resposta HTTP.

---

## 🛠️ Requisitos Técnicos

- ☕ **Java 25**  
- 🚀 **Spring Boot 4.0.1**  
- 🌐 **Spring Web**  
- 🗄️ **Spring Data JPA**  
- 💾 **Banco de dados H2** (em memória)  
- 🖥️ **Console H2** acessível em `/h2-console`  
- 📚 **Springdoc OpenAPI 2.7.0** (Swagger)

---

## 🧱 Arquitetura

Camadas implementadas no projeto:

- 📂 **controller** (obrigatório)
- 📂 **service** (boa prática)
- 📂 **repository** (obrigatório)
- 📂 **model** (entities)
- 📂 **config** (Swagger)

### ⚠️ Regras importantes
- ❌ Não utilizar DTO (conforme desafio)
- ✅ Usar entidades diretamente no JSON (`@RequestBody`)  
- 🌱 Popular dados iniciais com `CommandLineRunner`  

---

## 🧩 Modelagem de Dados

### 📁 Entidade Categoria
```java
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Produto> produtos;
}
```

**JSON:**
```json
{
  "id": 1,
  "nome": "Informática"
}
```

---

### 📦 Entidade Produto
```java
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private Double preco;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

**JSON:**
```json
{
  "id": 1,
  "nome": "Mouse Logitech",
  "preco": 120.0,
  "categoria": {
    "id": 1,
    "nome": "Informática"
  }
}
```

---

### 🔗 Relacionamento
- Uma **Categoria** possui vários **Produtos** (`@OneToMany`)
- Um **Produto** pertence a uma **Categoria** (`@ManyToOne`)

---

## 🌐 Endpoints da API

### 1️⃣ Criar Categoria
```http
POST /categorias
Content-Type: application/json

{
  "nome": "Informática"
}
```

**Resposta:** `201 Created`

---

### 2️⃣ Listar Categorias
```http
GET /categorias
```

**Resposta:** `200 OK`

```json
[
  {
    "id": 1,
    "nome": "Informática"
  },
  {
    "id": 2,
    "nome": "Livros"
  }
]
```

---

### 3️⃣ Criar Produto em uma Categoria
```http
POST /categorias/{id}/produtos
Content-Type: application/json

{
  "nome": "Mouse Logitech",
  "preco": 120.0
}
```

**Respostas:**
- `201 Created` - Produto criado com sucesso
- `404 Not Found` - Categoria não encontrada

---

### 4️⃣ Criar Produto com Query Parameter
```http
POST /produtos?categoriaId=1
Content-Type: application/json

{
  "nome": "Teclado Mecânico",
  "preco": 350.0
}
```

**Respostas:**
- `201 Created` - Produto criado com sucesso
- `404 Not Found` - Categoria não encontrada

---

### 5️⃣ Listar Produtos de uma Categoria
```http
GET /categorias/{id}/produtos
```

**Respostas:**
- `200 OK` - Lista de produtos
- `404 Not Found` - Categoria não encontrada

---

### 6️⃣ Listar Todos os Produtos
```http
GET /produtos
```

**Resposta:** `200 OK`

```json
[
  {
    "id": 1,
    "nome": "Mouse Logitech",
    "preco": 120.0,
    "categoria": {
      "id": 1,
      "nome": "Informática"
    }
  },
  {
    "id": 2,
    "nome": "Clean Code",
    "preco": 89.90,
    "categoria": {
      "id": 2,
      "nome": "Livros"
    }
  }
]
```

---

## 📡 Regras de Resposta HTTP

| Situação | Status Code |
|----------|-------------|
| Criação bem-sucedida | `201 Created` |
| Busca bem-sucedida | `200 OK` |
| Recurso não encontrado | `404 Not Found` |
| Dados inválidos | `400 Bad Request` |

✅ Todas as respostas utilizam `ResponseEntity`

---

## 🌱 Seed Inicial (CommandLineRunner)

Ao iniciar a aplicação, são criados automaticamente:

**Categorias:**
1. Informática
2. Livros

**Produtos:**
1. Mouse Logitech (R$ 120,00) - Informática
2. Teclado Mecânico (R$ 350,00) - Informática
3. Livro Clean Code (R$ 89,90) - Livros

---

## 📚 Documentação Swagger

A API possui documentação interativa completa usando **Swagger UI**.

### 🌐 Acessar a Documentação

Após iniciar a aplicação, acesse:

- **Swagger UI (Interface Visual):**  
  http://localhost:8080/swagger-ui.html

- **OpenAPI JSON:**  
  http://localhost:8080/api-docs

### ✨ Recursos do Swagger

✅ Visualização de todos os endpoints  
✅ Descrições detalhadas de cada operação  
✅ Exemplos de requisições e respostas  
✅ Schemas das entidades  
✅ Botão "Try it out" para testar endpoints  
✅ Códigos de status HTTP documentados  

---

## ✅ O que foi implementado

✔️ Spring Boot 4.0.1 configurado  
✔️ Java 25  
✔️ Estrutura em camadas (controller / service / repository)  
✔️ Banco H2 em memória  
✔️ Console H2 funcional  
✔️ Relacionamento `@OneToMany` / `@ManyToOne`  
✔️ Todos os endpoints conforme especificação  
✔️ `ResponseEntity` em todas as respostas  
✔️ Status codes corretos (200, 201, 404)  
✔️ Tratamento de erros 404  
✔️ Seed inicial com `CommandLineRunner`  
✔️ Sem uso de DTO  
✔️ **Documentação completa com Swagger**  

---

## ▶️ Como executar o projeto

### Pré-requisitos
- Java 25 instalado
- Gradle ou usar o wrapper incluído

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/mauricioandrade/desafio

# 2. Entrar no diretório
cd desafio

# 3. Executar a aplicação
./gradlew bootRun

# Ou no Windows
gradlew.bat bootRun
```

### 🌐 URLs Disponíveis

| Recurso | URL |
|---------|-----|
| 🚀 API Base | http://localhost:8080 |
| 📚 Swagger UI | http://localhost:8080/swagger-ui.html |
| 📄 OpenAPI Docs | http://localhost:8080/api-docs |
| 🗄️ H2 Console | http://localhost:8080/h2-console |

---

## 🧪 Console H2

Acesse o banco de dados em memória:

- **URL:** http://localhost:8080/h2-console  
- **JDBC URL:** `jdbc:h2:mem:testdb`  
- **Username:** `sa`  
- **Password:** *(deixar em branco)*

---

## 🧪 Testando a API

### Via Swagger UI (Recomendado)
1. Acesse http://localhost:8080/swagger-ui.html
2. Escolha um endpoint
3. Clique em "Try it out"
4. Preencha os dados e clique em "Execute"

### Via cURL

```bash
# Criar categoria
curl -X POST http://localhost:8080/categorias \
  -H "Content-Type: application/json" \
  -d '{"nome":"Eletrônicos"}'

# Listar categorias
curl http://localhost:8080/categorias

# Criar produto
curl -X POST http://localhost:8080/categorias/1/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Mouse Gamer","preco":150.0}'

# Listar produtos
curl http://localhost:8080/produtos
```

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 25 | Linguagem de programação |
| Spring Boot | 4.0.1 | Framework web |
| Spring Data JPA | 4.0.1 | Persistência de dados |
| H2 Database | 2.x | Banco em memória |
| Springdoc OpenAPI | 2.7.0 | Documentação Swagger |
| Gradle | 8.x | Gerenciador de dependências |

---

## 📂 Estrutura do Projeto

```
desafio-modulo1-java10x/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mauricioandrade/desafio_modulo1_java10x/
│   │   │       ├── DesafioModulo1Java10xApplication.java
│   │   │       ├── config/
│   │   │       │   └── SwaggerConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── CategoriaController.java
│   │   │       │   └── ProdutoController.java
│   │   │       ├── model/
│   │   │       │   ├── Categoria.java
│   │   │       │   └── Produto.java
│   │   │       ├── repository/
│   │   │       │   ├── CategoriaRepository.java
│   │   │       │   └── ProdutoRepository.java
│   │   │       └── service/
│   │   │           ├── CategoriaService.java
│   │   │           └── ProdutoService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── build.gradle
└── README.md
```

---

## 👨‍💻 Autor

**Maurício Andrade**  
- 🐙 GitHub: [@mauricioandrade](https://github.com/mauricioandrade)
---

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais como parte de um desafio técnico.

---

## 🎯 Conclusão

Este projeto demonstra a implementação de uma API REST completa seguindo as melhores práticas do Spring Boot, incluindo:

- ✅ Arquitetura em camadas
- ✅ Relacionamentos JPA
- ✅ Tratamento de erros
- ✅ Status codes corretos
- ✅ Documentação completa com Swagger
- ✅ Código limpo e organizado

---

