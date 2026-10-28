package br.com.financer.dao;

import br.com.financer.database.ConexaoMySQL;
import br.com.financer.factory.LancamentoFactory;
import br.com.financer.model.Lancamento;
import br.com.financer.model.TipoLancamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LancamentoDAO {
    private final LancamentoFactory lancamentoFactory = new LancamentoFactory();

    public void salvar(Lancamento lancamento) {
        String sql = """
                INSERT INTO lancamentos (tipo, descricao, valor, data, categoria, comentario)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(statement, lancamento);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lancamento.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel salvar o lancamento.", exception);
        }
    }

    public List<Lancamento> listarTodos() {
        return consultar("""
                SELECT id, tipo, descricao, valor, data, categoria, comentario
                FROM lancamentos
                ORDER BY data DESC, id DESC
                """);
    }

    public List<Lancamento> listarPorPeriodo(int mes, int ano) {
        String sql = """
                SELECT id, tipo, descricao, valor, data, categoria, comentario
                FROM lancamentos
                WHERE MONTH(data) = ? AND YEAR(data) = ?
                ORDER BY data DESC, id DESC
                """;

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, mes);
            statement.setInt(2, ano);
            return executarConsulta(statement);
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel filtrar por periodo.", exception);
        }
    }

    public void editar(Lancamento lancamento) {
        if (lancamento.getId() == null) {
            throw new IllegalArgumentException("Selecione um lancamento salvo para editar.");
        }

        String sql = """
                UPDATE lancamentos
                SET tipo = ?, descricao = ?, valor = ?, data = ?, categoria = ?, comentario = ?
                WHERE id = ?
                """;

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            preencherStatement(statement, lancamento);
            statement.setLong(7, lancamento.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel editar o lancamento.", exception);
        }
    }

    public void remover(Long id) {
        String sql = "DELETE FROM lancamentos WHERE id = ?";

        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel remover o lancamento.", exception);
        }
    }

    private List<Lancamento> consultar(String sql) {
        try (Connection connection = ConexaoMySQL.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return executarConsulta(statement);
        } catch (SQLException exception) {
            throw new RuntimeException("Nao foi possivel consultar os lancamentos.", exception);
        }
    }

    private List<Lancamento> executarConsulta(PreparedStatement statement) throws SQLException {
        List<Lancamento> lancamentos = new ArrayList<>();

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lancamentos.add(mapear(resultSet));
            }
        }

        return lancamentos;
    }

    private Lancamento mapear(ResultSet resultSet) throws SQLException {
        return lancamentoFactory.criar(
                TipoLancamento.valueOf(resultSet.getString("tipo")),
                resultSet.getLong("id"),
                resultSet.getString("descricao"),
                resultSet.getBigDecimal("valor"),
                resultSet.getDate("data").toLocalDate(),
                resultSet.getString("categoria"),
                resultSet.getString("comentario")
        );
    }

    private void preencherStatement(PreparedStatement statement, Lancamento lancamento) throws SQLException {
        statement.setString(1, lancamento.getTipo().name());
        statement.setString(2, lancamento.getDescricao());
        statement.setBigDecimal(3, lancamento.getValor());
        statement.setDate(4, Date.valueOf(lancamento.getData()));
        statement.setString(5, lancamento.getNomeCategoria());
        statement.setString(6, lancamento.getComentario());
    }
}
