
## Como rodar o banco de dados localmente (via Cloud SQL Proxy)

Siga os passos abaixo para se conectar ao banco de dados do Google Cloud na sua máquina local:

### 1. Autentique-se no GCP

Certifique-se de estar logado com sua conta Google Cloud:

```bash
gcloud auth login
```

### 2. Instale o Cloud SQL Auth Proxy

Baixe e dê permissão de execução:

```bash
wget https://storage.googleapis.com/cloud-sql-connectors/cloud-sql-proxy/v2.10.1/cloud-sql-proxy.linux.amd64 -O cloud-sql-proxy
chmod +x cloud-sql-proxy
```

### 3. Execute o proxy

Substitua `project_id`, `region` e `instance_id` pelos valores corretos do seu projeto:

```bash
./cloud-sql-proxy --port=5433 project_id:us-east1:instance_id
```

**Exemplo:**

```bash
./cloud-sql-proxy --port=5433 e-452020-e7:us-east1:sage
```

### 4. Por que usar a porta 5433?

A porta `5433` é usada para evitar conflitos com o PostgreSQL local, que geralmente roda na porta padrão `5432`.

### 5. Conexão ao banco

Depois de rodar o proxy, você pode se conectar ao banco usando:

- **Host:** `127.0.0.1`
- **Porta:** `5433`
- **Usuário, senha e nome do banco:** conforme definidos na sua instância do GCP

### Observação

Você pode utilizar ferramentas como **DBeaver**, **TablePlus** ou o terminal com `psql` para realizar a conexão com o banco.
