# Como Executar o Projeto Pedala

Este guia descreve como configurar e executar o ambiente de desenvolvimento completo do projeto Pedala (backend, frontend e banco de dados) usando Docker.

## Pré-requisitos

-   **Docker e Docker Compose**: Certifique-se de que o [Docker Desktop](https://www.docker.com/products/docker-desktop/) está instalado e em execução na sua máquina.

## Passos para Execução

1.  **Navegue até a Pasta Raiz**:
    Abra um terminal e navegue até a pasta `pedalando`, onde o arquivo `docker-compose.yml` está localizado.

2.  **Construa e Inicie os Contêineres**:
    Execute o seguinte comando no seu terminal. Este comando irá construir a imagem da aplicação (se ainda não tiver sido construída), baixar a imagem do MySQL e iniciar ambos os serviços em background.

    ```bash
    docker-compose up --build -d
    ```

    -   `--build`: Força a reconstrução da imagem da API caso haja alguma alteração no código-fonte ou no `Dockerfile`.
    -   `-d`: (Detached mode) Executa os contêineres em segundo plano.

3.  **Acesse a Aplicação**:
    Após a conclusão do comando, o ambiente estará pronto:

    -   **Backend (API)**: A API Spring Boot estará acessível em `http://localhost:8080`.
    -   **Frontend**: Para acessar a interface do usuário, abra o arquivo `frontend/index.html` diretamente no seu navegador.
    -   **Banco de Dados**: O banco de dados MySQL estará acessível na porta `3306` (para ferramentas como DBeaver, use `localhost:3306` com usuário `root` e senha `root`).

## Gerenciando o Ambiente

-   **Verificar Logs**:
    Para ver os logs dos contêineres em tempo real, use:
    ```bash
    docker-compose logs -f
    ```
    Para ver os logs de um serviço específico (ex: `api`):
    ```bash
    docker-compose logs -f api
    ```

-   **Parar os Contêineres**:
    Para parar todos os serviços sem remover os dados:
    ```bash
    docker-compose down
    ```

-   **Recriar o Banco de Dados (Reset)**:
    Se você precisar apagar todos os dados do banco de dados e começar do zero (útil ao adicionar novos scripts de migração SQL), siga estes passos:
    ```bash
    # 1. Pare e remova os contêineres
    docker-compose down

    # 2. Remova o volume do MySQL (ATENÇÃO: ISSO APAGA TODOS OS DADOS)
    docker volume rm pedalando_pedala-mysql-data

    # 3. Inicie tudo novamente
    docker-compose up --build -d
    ```

## Credenciais Padrão

-   **Usuário Admin**: `admin@pedala.com` / `123456`
-   **Usuário Funcionário**: `funcionario@pedala.com` / `123456`
-   **Usuário Cliente**: `user@pedala.com` / `123456`

[seed] admin criado: admin@pedala.com
[seed] funcionario criado: funcionario@pedala.com

Pedala API v3 rodando na porta 3001
  Health:  http://localhost:3001/api/health

  Admin:       admin@pedala.com / admin123
  Funcionario: funcionario@pedala.com / funcionario123
```

> Os usuarios admin e funcionario sao criados **automaticamente** na inicializacao.
> Nao e necessario rodar nenhum seed manual.

---

## 4. Abrir o frontend

Abra os arquivos HTML diretamente no navegador (nao precisa de servidor web):

- `frontend/index.html` — Site principal
- `frontend/login.html` — Login
- `frontend/register.html` — Cadastro de usuario
- `frontend/dashboard.html` — Painel do cliente
- `frontend/admin.html` — Painel do administrador
- `frontend/employee.html` — Painel do funcionario

> **Dica:** Use a extensao **Live Server** no VS Code para hot-reload automatico.

---

## 5. Rodar o simulador GPS (opcional)

Em um segundo terminal, na **raiz do projeto** (nao dentro de `backend/`):

```bash
node gps_simulator.js
```

O simulador:
- Detecta automaticamente bikes em locacao
- Simula rotas em Sao Paulo (Centro, Paulista, Ibirapuera, Jardins)
- Envia posicoes a cada 5 segundos
- Exibe logs de posicao no terminal

Nos paineis Admin e Funcionario, a aba **Rastreamento GPS** atualizara automaticamente.

---

## Credenciais padrao

| Perfil | Email | Senha |
|--------|-------|-------|
| Admin | admin@pedala.com | admin123 |
| Funcionario | funcionario@pedala.com | funcionario123 |

Para criar um usuario normal: cadastre em `frontend/register.html`.

---

## Endpoints principais

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | /api/health | Status da API |
| POST | /api/auth/login | Login |
| POST | /api/auth/register | Cadastro |
| GET | /api/bikes | Lista bikes |
| POST | /api/rentals | Criar locacao |
| GET | /api/rentals/meus | Minhas locacoes |
| PUT | /api/rentals/:id/renovar | Renovar contrato |
| GET | /api/contratos/:id | Baixar contrato HTML |
| GET | /api/admin/stats | Estatisticas (admin) |
| GET | /api/gps | Posicoes GPS (admin/func) |
| GET | /api/gps/:bikeId | Historico de rotas da bike |
| GET | /api/vistorias | Lista vistorias (func/admin) |

### Qualidade de codigo (novo)

No diretório `backend`, também estão disponíveis:

```bash
npm run test:e2e          # valida fluxo ponta a ponta (contratos front/back)
npm run lint              # lint JS backend + frontend modular
npm run typecheck:frontend
npm run format
```

---

## Parar o servidor

Pressione `Ctrl + C` no terminal onde o servidor esta rodando.

> **Atencao (POC):** Os dados ficam em memoria e sao perdidos ao reiniciar o servidor.
> Para dados persistentes, veja `PROXIMOS_PASSOS.md`.
