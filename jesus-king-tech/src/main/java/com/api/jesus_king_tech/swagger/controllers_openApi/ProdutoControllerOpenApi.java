package com.api.jesus_king_tech.swagger.controllers_openApi;

import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Produtos", description = "Gerencia os produtos")
public interface ProdutoControllerOpenApi {

    ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto);

    ResponseEntity<String> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoAtualizado);

    ResponseEntity<String> deletarProduto(@PathVariable Integer id);

    ResponseEntity<String> cadastrarCategoria(@RequestBody Categoria categoria);

    ResponseEntity<String> atualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaAtualizada);

    ResponseEntity<String> deletarCategoria(@PathVariable Integer id);

    ResponseEntity<String> cadastrarTipo(@RequestBody Tipo tipo);

    ResponseEntity<String> atualizarTipo(@PathVariable Integer id, @RequestBody Tipo tipoAtualizado);

    ResponseEntity<String> deletarTipo(@PathVariable Integer id);

    ResponseEntity<List<CategoriaDTO>> listarTodasCategorias();

    ResponseEntity<List<TipoDTO>> listarTodosTipos();

    ResponseEntity<List<ProdutoDTO>> listarTodosProdutos();

}
