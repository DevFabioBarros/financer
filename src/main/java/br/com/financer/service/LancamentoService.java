package br.com.financer.service;

import br.com.financer.dao.LancamentoDAO;
import br.com.financer.factory.LancamentoFactory;
import br.com.financer.model.Lancamento;
import br.com.financer.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LancamentoService {
    private final LancamentoDAO dao = new LancamentoDAO();
    private final LancamentoFactory factory = new LancamentoFactory();

    public Lancamento cadastrar(TipoLancamento tipo, String descricao, BigDecimal valor, LocalDate data, String categoria, String comentario) {
        Lancamento lancamento = factory.criar(tipo, null, descricao, valor, data, categoria, comentario);
        dao.salvar(lancamento);
        return lancamento;
    }

    public Lancamento editar(Long id, TipoLancamento tipo, String descricao, BigDecimal valor, LocalDate data, String categoria, String comentario) {
        Lancamento lancamento = factory.criar(tipo, id, descricao, valor, data, categoria, comentario);
        dao.editar(lancamento);
        return lancamento;
    }

    public void remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Selecione um lancamento para remover.");
        }
        dao.remover(id);
    }

    public List<Lancamento> listar() {
        return dao.listarTodos();
    }

    public List<Lancamento> filtrarPorPeriodo(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes invalido.");
        }
        if (ano < 1) {
            throw new IllegalArgumentException("Ano invalido.");
        }
        return dao.listarPorPeriodo(mes, ano);
    }
}
