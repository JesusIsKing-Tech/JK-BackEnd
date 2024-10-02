package com.api.jesus_king_tech.domain.produto.tipo.dto;

import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;

public class TipoDTO {

    private Integer id;
    private String nome;
    private CategoriaDTO categoria;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }
}
