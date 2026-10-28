package br.com.financer.factory;

import br.com.financer.model.Categoria;
import br.com.financer.model.Despesa;
import br.com.financer.model.Investimento;
import br.com.financer.model.Lancamento;
import br.com.financer.model.Receita;
import br.com.financer.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoFactory {
    public Lancamento criar(TipoLancamento tipo, Long id, String descricao, BigDecimal valor, LocalDate data, String categoria, String comentario) {
        Categoria categoriaLancamento = new Categoria(categoria);

        if (tipo == TipoLancamento.RECEITA) {
            return new Receita(id, descricao, valor, data, categoriaLancamento, comentario);
        }

        if (tipo == TipoLancamento.DESPESA) {
            return new Despesa(id, descricao, valor, data, categoriaLancamento, comentario);
        }

        if (tipo == TipoLancamento.INVESTIMENTO) {
            return new Investimento(id, descricao, valor, data, categoriaLancamento, comentario);
        }

        throw new IllegalArgumentException("Tipo de lancamento invalido.");
    }
}
