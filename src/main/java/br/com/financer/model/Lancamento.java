package br.com.financer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Lancamento {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Categoria categoria;
    private String comentario;

    protected Lancamento(Long id, String descricao, BigDecimal valor, LocalDate data, Categoria categoria, String comentario) {
        this.id = id;
        setDescricao(descricao);
        setValor(valor);
        setData(data);
        setCategoria(categoria);
        setComentario(comentario);
    }

    public abstract TipoLancamento getTipo();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descricao e obrigatoria.");
        }
        this.descricao = descricao.trim();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data e obrigatoria.");
        }
        this.data = data;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getNomeCategoria() {
        return categoria.getNome();
    }

    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria e obrigatoria.");
        }
        this.categoria = categoria;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario == null ? "" : comentario.trim();
    }
}
