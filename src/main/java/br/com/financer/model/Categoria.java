package br.com.financer.model;

import java.util.Objects;

public class Categoria {
    private final String nome;

    public Categoria(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("A categoria e obrigatoria.");
        }
        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria categoria)) {
            return false;
        }
        return nome.equalsIgnoreCase(categoria.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }
}
