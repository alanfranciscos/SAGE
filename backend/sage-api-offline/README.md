# SAGE API (Backend)

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-green?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-24.0+-blue?style=for-the-badge&logo=docker)

## 📖 Visão Geral

O **SAGE API** é o serviço de backend responsável pelo gerenciamento do sistema **SAGE** (Sistema de Assistência e Gerenciamento de Emergências). Ele fornece uma API RESTful robusta para controlar alarmes, cuidadores, residentes e solicitações de assistência em ambientes de cuidados, como lares de idosos ou hospitais.

O sistema foi projetado para ser escalável e fornecer atualizações em tempo real para os cuidadores, garantindo uma resposta rápida a emergências.

---

## 🚀 Funcionalidades Principais

*   **Gestão de Residentes:** CRUD completo, ativação/desativação, upload de fotos e histórico de chamadas.
*   **Gestão de Cuidadores:** Cadastro, autenticação segura (JWT) e controle de acesso.
*   **Monitoramento de Assistência:**
    *   Registro de chamadas de emergência e avisos.
    *   Atribuição de cuidadores a chamadas.
    *   Finalização e histórico de atendimentos.
*   **Notificações em Tempo Real:** Uso de **Server-Sent Events (SSE)** para notificar interfaces de monitoramento sobre novas chamadas instantaneamente.
*   **Integração com Hardware:** Gestão de dispositivos de alarme e controles remotos associados aos residentes.
*   **Relatórios:** Geração de relatórios operacionais e estatísticas de atendimento.

---

## 🛠️ Stack Tecnológica

*   **Linguagem:** Java 17
*   **Framework:** Spring Boot 3.4.5
*   **Banco de Dados:** PostgreSQL 15
*   **Autenticação:** Spring Security + JWT
*   **Documentação:** SpringDoc OpenAPI (Swagger UI)
*   **Containerização:** Docker & Docker Compose
*   **Build Tool:** Maven

---

## 📋 Pré-requisitos

Para executar este projeto localmente, você precisará ter instalado:

*   [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
*   [Maven](https://maven.apache.org/)
*   [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)

---

## 🔧 Configuração e Instalação

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/sage-api.git
    cd sage-api
    ```

2.  **Configuração do Banco de Dados:**
    O projeto utiliza PostgreSQL. As configurações padrão estão em `src/main/resources/application.properties`.
    
    As principais variáveis de ambiente (ou propriedades) são:
    *   `DB_URL`: URL de conexão JDBC (Padrão: `jdbc:postgresql://localhost:5432/sage` em ambiente local ou definido no Docker)
    *   `SQL_USERNAME`: Usuário do banco (Padrão: `postgres`)
    *   `SQL_PASSWORD`: Senha do banco (Padrão: `postgres`)

3.  **Scripts de Inicialização:**
    Os scripts de criação do banco estão localizados na pasta `infra/`:
    *   `database.sql`: Schema do banco.
    *   `inserts.sql`: Dados iniciais para testes.

---

## ▶️ Executando a Aplicação

O projeto inclui um `Makefile` para simplificar os comandos comuns.

### Usando Docker (Recomendado)

Para rodar toda a stack (API + Banco de Dados):

```bash
make run-application
```
*Isso irá construir a imagem da API e iniciar os containers definidos em `docker-compose-run-application.yaml`.*

### Ambiente de Desenvolvimento Local

Para rodar serviços auxiliares (Banco de Dados) enquanto você roda a API na sua IDE:

```bash
make local-environment
```

### Apenas o Banco de Dados

```bash
make run-database
```

### Compilando o Projeto

```bash
make install
# Ou via Maven direto:
./mvnw clean install
```

---

## 📚 Documentação da API

Com a aplicação rodando, você pode acessar a documentação interativa (Swagger UI) em:

👉 **[http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)**

---

## 🧪 Testes

O projeto possui testes unitários e de integração utilizando JUnit 5 e Mockito.

Para executar os testes:

```bash
./mvnw test
```

---

## 📂 Estrutura do Projeto

```
src/main/java/com/sage
├── config/          # Configurações (Security, CORS, etc.)
├── controller/      # Controladores REST (Endpoints)
├── dao/             # Implementações de acesso a dados (Repository Pattern)
├── dto/             # Objetos de Transferência de Dados (Request/Response)
├── model/           # Entidades de Domínio
├── port/            # Interfaces para Serviços e DAOs (Hexagonal Arch. concepts)
├── service/         # Regras de Negócio
└── SageApplication.java  # Classe Principal
```

---

**Desenvolvido por:** Equipe SAGE
