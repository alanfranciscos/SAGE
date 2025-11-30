# SAGE - Sistema de Apoio e Gerenciamento em Enfermagem

Este repositório contém o código-fonte e documentação do projeto SAGE, um sistema de chamada de enfermeiros e gestão de residentes. O projeto é composto por uma API Backend em Java/Spring Boot, um Frontend em Angular e scripts de infraestrutura com Terraform.

## 📂 Estrutura do Projeto

*   **`backend/`**: Contém o código-fonte da API.
    *   `sage-api-offline`: Versão principal da API (Spring Boot).
*   **`frontend/`**: Contém o código-fonte da interface web.
    *   `sage-frontend-offline`: Aplicação Angular.
*   **`docs/`**: Documentação funcional, técnica e de gerenciamento do projeto (Diagramas, Casos de Uso, Dicionário de Dados, etc.).
*   **`terraform/`**: Scripts para provisionamento de infraestrutura no Google Cloud Platform (GCP).
*   **`.github/workflows/`**: Pipelines de CI/CD para deploy automatizado.

## 🚀 Tecnologias Utilizadas

*   **Backend:** Java 17, Spring Boot 3, PostgreSQL, Maven, Docker.
*   **Frontend:** Angular 18+, TypeScript, SCSS.
*   **Infraestrutura:** Terraform, Docker Compose.
*   **Cloud:** Google Cloud Platform (Cloud Run, Artifact Registry, Cloud SQL).

## ⚙️ Pré-requisitos

Para executar o projeto localmente, você precisará das seguintes ferramentas instaladas:

*   [Java JDK 17+](https://adoptium.net/)
*   [Node.js (LTS)](https://nodejs.org/)
*   [Docker & Docker Compose](https://www.docker.com/)
*   [Maven](https://maven.apache.org/)
*   [Angular CLI](https://angular.io/cli) (`npm install -g @angular/cli`)

## 🏃‍♂️ Como Executar Localmente

### Backend (`backend/sage-api-offline`)

O backend possui um `Makefile` para facilitar a execução dos serviços.

1.  Navegue até o diretório do backend:
    ```bash
    cd backend/sage-api-offline
    ```

2.  Opção A: Rodar tudo via Docker (API + Banco de Dados):
    ```bash
    make run-application
    ```
    Isso subirá o banco de dados PostgreSQL e a aplicação conectada.

3.  Opção B: Rodar apenas o banco via Docker e a API via Maven (Recomendado para desenvolvimento):
    *   Inicie o banco de dados:
        ```bash
        make run-database
        ```
    *   Em outro terminal, inicie a aplicação:
        ```bash
        ./mvnw spring-boot:run
        ```
        (Ou `mvn spring-boot:run` se tiver o Maven instalado).

A API estará disponível em `http://localhost:8080`.
Documentação Swagger: `http://localhost:8080/api/swagger-ui/index.html` (quando a aplicação estiver rodando).

### Frontend (`frontend/sage-frontend-offline`)

1.  Navegue até o diretório do frontend:
    ```bash
    cd frontend/sage-frontend-offline
    ```

2.  Instale as dependências:
    ```bash
    npm install
    ```

3.  Execute o servidor de desenvolvimento:
    ```bash
    ng serve
    ```
    Acesse a aplicação em `http://localhost:4200/`. O frontend está configurado para usar um proxy (`proxy.conf.json`) para redirecionar chamadas de API para o backend.

## ☁️ Infraestrutura e Deploy

O projeto utiliza **Terraform** para gerenciar a infraestrutura no GCP. Os módulos estão localizados em `terraform/modules`:
*   `bucket`: Cloud Storage.
*   `artifact_registry`: Armazenamento de imagens Docker.
*   `cloud_run`: Execução da API Serverless.
*   `sql_database`: Banco de dados gerenciado.

O deploy é automatizado via **GitHub Actions** (veja `.github/workflows/backend_sage_api_deploy.yml`), que realiza:
1.  Provisionamento da infraestrutura com Terraform.
2.  Build e Push da imagem Docker da API.
3.  Deploy no Cloud Run.

## 📚 Documentação Adicional

Para detalhes aprofundados sobre requisitos, modelagem de dados e arquitetura, consulte a pasta [`docs/`](docs/). Lá você encontrará:
*   Dicionário de Dados.
*   Diagramas UML (Classes, Sequência, Casos de Uso).
*   Protótipos de Tela.
*   Manuais e Relatórios de Desempenho.
