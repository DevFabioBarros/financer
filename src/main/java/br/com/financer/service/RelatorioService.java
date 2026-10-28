package br.com.financer.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.financer.dao.LancamentoDAO;
import br.com.financer.model.DesempenhoMensal;
import br.com.financer.model.Lancamento;

public class RelatorioService {
    private final LancamentoDAO lancamentoDAO = new LancamentoDAO();

    public List<DesempenhoMensal> visualizarDesempenhoMensal() {
        Map<YearMonth, BigDecimal> receitasPorMes = new LinkedHashMap<>();
        Map<YearMonth, BigDecimal> despesasPorMes = new LinkedHashMap<>();
        Map<YearMonth, BigDecimal> investimentosPorMes = new LinkedHashMap<>();

        List<Lancamento> lancamentos = lancamentoDAO.listarTodos();
        lancamentos.stream()
                .map(lancamento -> YearMonth.from(lancamento.getData()))
                .distinct()
                .sorted()
                .forEach(mes -> {
                    receitasPorMes.put(mes, BigDecimal.ZERO);
                    despesasPorMes.put(mes, BigDecimal.ZERO);
                    investimentosPorMes.put(mes, BigDecimal.ZERO);
                });

        for (Lancamento lancamento : lancamentos) {
            YearMonth mes = YearMonth.from(lancamento.getData());
            if (null != lancamento.getTipo()) switch (lancamento.getTipo()) {
                case RECEITA -> receitasPorMes.merge(mes, lancamento.getValor(), BigDecimal::add);
                case DESPESA -> despesasPorMes.merge(mes, lancamento.getValor(), BigDecimal::add);
                case INVESTIMENTO -> investimentosPorMes.merge(mes, lancamento.getValor(), BigDecimal::add);
                default -> {
                }
            }
        }

        List<DesempenhoMensal> desempenho = new ArrayList<>();
        receitasPorMes.keySet().forEach(mes -> desempenho.add(new DesempenhoMensal(
                mes,
                receitasPorMes.getOrDefault(mes, BigDecimal.ZERO),
                despesasPorMes.getOrDefault(mes, BigDecimal.ZERO),
                investimentosPorMes.getOrDefault(mes, BigDecimal.ZERO)
        )));

        return desempenho;
    }
}
