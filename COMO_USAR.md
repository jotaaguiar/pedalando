# Como Usar — Pedala Backend

Guia passo a passo para rodar o Pedala localmente.

---

## Pre-requisitos

- **Node.js** versao 16 ou superior: https://nodejs.org/
- **Terminal** (PowerShell, CMD ou bash)

Verifique se o Node esta instalado:
```bash
node -v
npm -v
```

---

## 1. Instalar dependencias

```bash
cd backend
npm install
```

---

## 2. Arquivo de configuracao (.env)

Crie o arquivo `backend/.env` com o conteudo abaixo:

```env
PORT=3001
JWT_SECRET=pedala-secret-2025
GPS_API_KEY=pedala-gps-2025
```

> Troque `JWT_SECRET` por uma chave forte em producao.

---

## 3. Rodar o servidor

```bash
node server.js
```

**Saida esperada:**
```
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
