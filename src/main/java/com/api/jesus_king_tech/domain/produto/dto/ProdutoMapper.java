package com.api.jesus_king_tech.domain.produto.dto;


import com.api.jesus_king_tech.domain.cesta_basica.CestaBasica;
import com.api.jesus_king_tech.domain.cesta_basica.dto.CestaBasicaDTO;
import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {


    public ProdutoDTO toProdutoDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setPeso (produto.getPeso_litro());
        dto.setDataEntrada(produto.getDataEntrada());

        if (produto.getCategoria() != null) {
            dto.setCategoria(toCategoriaDTO(produto.getCategoria()));
        }

        if (produto.getTipo() != null) {
            dto.setTipo(toTipoDTO(produto.getTipo()));
        }

        return dto;
    }

    public CategoriaDTO toCategoriaDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        return dto;
    }

    public TipoDTO toTipoDTO(Tipo tipo) {
        TipoDTO dto = new TipoDTO();
        dto.setId(tipo.getId());
        dto.setNome(tipo.getNome());
        return dto;
    }

    public Produto toProdutoEntity(ProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setId(produtoDTO.getId());
        produto.setPeso_litro(produtoDTO.getPeso());
        produto.setDataEntrada(produtoDTO.getDataEntrada());

        if (produtoDTO.getCategoria() != null) {
            produto.setCategoria(toCategoriaEntity(produtoDTO.getCategoria()));
        }

//        if (produtoDTO.getTipo() != null) {
//            produto.setTipo(toTipoEntity(produtoDTO.getTipo()));
//        }

        return produto;
    }

    public Categoria toCategoriaEntity(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setId(categoriaDTO.getId());
        categoria.setNome(categoriaDTO.getNome());
        return categoria;
    }

    public Tipo toTipoEntity(TipoDTO tipoDTO) {
        Tipo tipo = new Tipo();
        tipo.setId(tipoDTO.getId());
        tipo.setNome(tipoDTO.getNome());
        return tipo;
    }

    public Produto toProdutoEntity(ProdutoMultiploDTO produtoDTO) {
        System.out.println("ProdutoDTO PESO: " + produtoDTO.getPeso_litro());
        System.out.println("ProdutoDTO DATA: " + produtoDTO.getDataEntrada());
        Produto produto = new Produto();
        produto.setPeso_litro(produtoDTO.getPeso_litro());
        produto.setDataEntrada(produtoDTO.getDataEntrada());
        produto.setTipo(toTipoEntity(produtoDTO.getTipo()));
        produto.setCategoria(toCategoriaEntity(produtoDTO.getCategoria()));

        if (produtoDTO.getCategoria() != null) {
            produto.setCategoria(toCategoriaEntity(produtoDTO.getCategoria()));
        }

        if (produtoDTO.getTipo() != null) {
            produto.setTipo(toTipoEntity(produtoDTO.getTipo()));
        }

        return produto;
    }

    public List<ProdutoDTO> toProdutoDTOList(List<Produto> produtos) {
        return produtos.stream()
                .map(this::toProdutoDTO)
                .collect(Collectors.toList());
    }

    public CestaBasicaDTO toCestaBasicaDTO(CestaBasica cestaBasica) {
        return CestaBasicaDTO.builder()
                .id(cestaBasica.getId())
                .dataMontagem(cestaBasica.getDataMontagem())
                .dataRetirada(cestaBasica.getDataRetirada())
                .produtos(toProdutoDTOList(cestaBasica.getProdutos()))
                .build();
    }
}

