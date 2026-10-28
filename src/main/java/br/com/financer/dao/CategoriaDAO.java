package br.com.financer.dao;

import br.com.financer.database.ConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private static final List<String> CATEGORIAS_PADRAO = List.of(
            "Casa",
            "Lazer",
            "Investimento",
            "Alimentacao",
            "Transporte",
            "Mercado"
    );

    public void salvar(String nome) {
        String categoria = normalizar(nome);
        String sql = "INSERT IGNORE INTO categorias (nome) VALUES (?)";

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel salvar a categoria.", exception);
        }
    }

    public List<String> listar() {
        String sql = "SELECT nome FROM categorias ORDER BY nome";

        try (Connection connection = ConexaoMySQL.conectar();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<String> categorias = new ArrayList<>();
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                if (nome != null && !nome.isBlank()) {
                    categorias.add(nome);
                }
            }
            return categorias;
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel listar as categorias.", exception);
        }
    }

    public void remover(String nome) {
        String categoria = normalizar(nome);
        String sql = "DELETE FROM categorias WHERE nome = ?";

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel remover a categoria.", exception);
        }
    }

    public void garantirPadroes() {
        for (String categoria : CATEGORIAS_PADRAO) {
            salvar(categoria);
        }
    }

    private String normalizar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome da categoria.");
        }
        return nome.trim();
    }
}
