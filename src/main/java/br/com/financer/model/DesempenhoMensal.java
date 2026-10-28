package br.com.financer.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class DesempenhoMensal {
    private final YearMonth mes;
    private final BigDecimal totalReceitas;
    private final BigDecimal totalDespesas;
    private final BigDecimal totalInvestimentos;
    private final BigDecimal saldo;

    public DesempenhoMensal(YearMonth mes, BigDecimal totalReceitas, BigDecimal totalDespesas, BigDecimal totalInvestimentos) {
        this.mes = mes;
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.totalInvestimentos = totalInvestimentos;
        this.saldo = totalReceitas.subtract(totalDespesas).subtract(totalInvestimentos);
    }

    public YearMonth getMes() {
        return mes;
    }

    public BigDecimal getTotalReceitas() {
        return totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public BigDecimal getTotalInvestimentos() {
        return totalInvestimentos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
