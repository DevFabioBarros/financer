# Financer

Aplicação desktop para organização financeira pessoal. Permite registrar receitas, despesas e investimentos, gerenciar categorias, filtrar lançamentos por período e consultar o desempenho mensal.

## Funcionalidades

- Cadastro e edição de receitas, despesas e investimentos.
- Listagem e filtro de lançamentos por período.
- Cadastro, listagem e remoção de categorias.
- Resumo do desempenho financeiro mensal.
- Persistência de dados em MySQL.

## Tecnologias

- Java 24
- JavaFX 24.0.2 (FXML e CSS)
- MySQL Connector/J 8.4.0
- Maven

## Requisitos

- JDK 24.
- Maven 3.9 ou compatível.
- MySQL 8 em execução.

## Configuração do banco

Por padrão, o aplicativo conecta a `jdbc:mysql://localhost:3306/financer` com usuário `root` e senha vazia. Ajuste esses valores para seu ambiente usando propriedades Java ou variáveis de ambiente:

| Propriedade Java | Variável de ambiente | Padrão |
| --- | --- | --- |
| `db.url` | `FINANCER_DB_URL` | `jdbc:mysql://localhost:3306/financer?createDatabaseIfNotExist=true&serverTimezone=America/Sao_Paulo` |
| `db.user` | `FINANCER_DB_USER` | `root` |
| `db.password` | `FINANCER_DB_PASSWORD` | vazio |

Exemplo no PowerShell:

```powershell
$env:FINANCER_DB_USER = "seu_usuario"
$env:FINANCER_DB_PASSWORD = "sua_senha"
```

O esquema e as categorias iniciais estão em `database/schema.sql`. O projeto também inicializa o banco ao abrir. `database/seed-test-data.sql` contém dados de teste; execute-o somente se quiser inserir esses registros.

## Executar

Na raiz do projeto:

```bash
mvn javafx:run
```

## Estrutura

- `src/main/java`: aplicação, controladores, modelos, serviços e acesso a dados.
- `src/main/resources`: tela JavaFX em FXML e estilos CSS.
- `database/`: esquema e dados opcionais de teste.
- `diagrama-arquitetura.mmd`, `diagrama-caso-uso.mmd`, `diagrama-classes.mmd`: diagramas Mermaid do projeto.

## Licença

Nenhuma licença foi definida. Consulte o autor antes de reutilizar ou distribuir o projeto.
