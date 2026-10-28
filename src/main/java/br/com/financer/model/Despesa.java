package br.com.financer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Despesa extends Lancamento {
    public Despesa(Long id, String descricao, BigDecimal valor, LocalDate data, Categoria categoria, String comentario) {
        super(id, descricao, valor, data, categoria, comentario);
    }

    @Override
    public TipoLancamento getTipo() {
        return TipoLancamento.DESPESA;
    }
}
