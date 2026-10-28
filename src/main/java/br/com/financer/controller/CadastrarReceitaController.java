package br.com.financer.controller;

import br.com.financer.model.Lancamento;
import br.com.financer.model.TipoLancamento;
import br.com.financer.service.LancamentoService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CadastrarReceitaController {
    private final LancamentoService service = new LancamentoService();

    public Lancamento cadastrar(String descricao, BigDecimal valor, LocalDate data, String categoria, String comentario) {
        return service.cadastrar(TipoLancamento.RECEITA, descricao, valor, data, categoria, comentario);
    }
}
