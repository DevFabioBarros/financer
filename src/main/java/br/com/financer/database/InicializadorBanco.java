package br.com.financer.database;

import br.com.financer.dao.CategoriaDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBanco {
    public void inicializar() {
        String sqlLancamentos = """
                CREATE TABLE IF NOT EXISTS lancamentos (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    tipo VARCHAR(20) NOT NULL,
                    descricao VARCHAR(120) NOT NULL,
                    valor DECIMAL(10, 2) NOT NULL,
                    data DATE NOT NULL,
                    categoria VARCHAR(60) NOT NULL,
                    comentario VARCHAR(255) NOT NULL DEFAULT ''
                )
                """;

        String sqlCategorias = """
                CREATE TABLE IF NOT EXISTS categorias (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(60) NOT NULL UNIQUE
                )
                """;

        try (Connection connection = ConexaoMySQL.conectar();
             Statement statement = connection.createStatement()) {
            statement.execute(sqlLancamentos);
            statement.execute(sqlCategorias);
            if (!colunaExiste(connection, "lancamentos", "comentario")) {
                statement.execute("ALTER TABLE lancamentos ADD COLUMN comentario VARCHAR(255) NOT NULL DEFAULT ''");
            }
            new CategoriaDAO().garantirPadroes();
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel inicializar o banco de dados.", exception);
        }
    }

    private boolean colunaExiste(Connection connection, String tabela, String coluna) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getColumns(null, null, tabela, coluna)) {
            return resultSet.next();
        }
    }
}

