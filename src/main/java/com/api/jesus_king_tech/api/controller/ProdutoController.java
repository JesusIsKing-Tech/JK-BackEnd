package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.cesta_basica.CestaBasica;
import com.api.jesus_king_tech.domain.cesta_basica.dto.CestaBasicaDTO;
import com.api.jesus_king_tech.domain.endereco.dto.ListaEstaticaEnderecoResponse;
import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMultiploDTO;
import com.api.jesus_king_tech.domain.produto.repository.ProdutoRepository;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import com.api.jesus_king_tech.service.CategoriaService;
import com.api.jesus_king_tech.service.ProdutoService;
import com.api.jesus_king_tech.service.TipoService;
import com.azure.core.annotation.Get;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {


    @Autowired
    private ProdutoRepository produtoRepository;

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final TipoService tipoService;

    @Autowired
    private ProdutoMapper produtoMapper;

    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService, TipoService tipoService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.tipoService = tipoService;
    }

    @PostMapping("/multiplo")
    public ResponseEntity<List<ProdutoDTO>> cadastrarProduto2(@RequestBody ProdutoMultiploDTO produtoMultiploDTO) {


        List<ProdutoDTO> produtosResponseDTO = new ArrayList<>();

        for (int i = 0; i < produtoMultiploDTO.getQuantidade() ; i++) {
            Produto produto = produtoMapper.toProdutoEntity(produtoMultiploDTO);
            ProdutoDTO produtoDTO = produtoService.cadastrarProduto(produto);
            produtosResponseDTO.add(produtoDTO);
        }

        return ResponseEntity.status(201).body(produtosResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody Produto produto) {
        ProdutoDTO produtoDTO = produtoService.cadastrarProduto(produto);
        return ResponseEntity.status(201).body(produtoDTO);
    }

//    @GetMapping("/cards")
//    public ResponseEntity<List<Produto>> listarCards () {
//        List<Produto> produtos = produtoService.listarTodosCards();
//        return ;
//    }


    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoAtualizado) {
        ProdutoDTO produtoDTO = produtoService.atualizarProduto(id, produtoAtualizado);
        if (produtoDTO != null) {
            return ResponseEntity.ok(produtoDTO);
        }

        return ResponseEntity.status(404).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Integer id) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        boolean deletado = produtoService.deletarProduto(id);
        if (deletado) {
            return ResponseEntity.ok("Produto removido com sucesso!");
        }

        return ResponseEntity.status(404).body("Produto não encontrado");
    }


    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> cadastrarCategoria(@RequestBody Categoria categoria) {
        CategoriaDTO categoriaDTO = categoriaService.cadastrarCategoria(categoria);
        return ResponseEntity.status(201).body(categoriaDTO);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> atualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaAtualizada) {
        CategoriaDTO categoriaDTO = categoriaService.atualizarCategoria(id, categoriaAtualizada);
        return ResponseEntity.ok(categoriaDTO);
    }


    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Integer id) {
        categoriaService.removerCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> listarTodasCategorias() {
        List<CategoriaDTO> categoriasDTO = categoriaService.listarTodasCategorias();
        return ResponseEntity.ok(categoriasDTO);
    }

    @PostMapping("/tipos")
    public ResponseEntity<TipoDTO> cadastrarTipo(@RequestBody Tipo tipo) {
        TipoDTO tipoDTO = tipoService.cadastrarTipo(tipo);
        return ResponseEntity.status(201).body(tipoDTO);
    }

    @PutMapping("/tipos/{id}")
    public ResponseEntity<TipoDTO> atualizarTipo(@PathVariable Integer id, @RequestBody Tipo tipoAtualizado) {
        TipoDTO tipoDTO = tipoService.atualizarTipo(id, tipoAtualizado);
        return ResponseEntity.ok(tipoDTO);
    }

    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<Void> deletarTipo(@PathVariable Integer id) {
        tipoService.removerTipo(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/tipos")
    public ResponseEntity<List<TipoDTO>> listarTodosTipos() {
        List<TipoDTO> tiposDTO = tipoService.listarTodosTipos();
        return ResponseEntity.ok(tiposDTO);
    }

    @GetMapping("/tipos/by-categoria/{id}")
    public ResponseEntity<List<TipoDTO>> listarTiposPorCategoria(@PathVariable Integer id) {
        List<TipoDTO> tiposDTO = tipoService.listarTiposPorCategoria(id);
        return ResponseEntity.ok(tiposDTO);
    }

    @PostMapping("/cesta-basica")
    public ResponseEntity<List<CestaBasicaDTO>> montarCesta() throws BadRequestException {
        List<CestaBasicaDTO> cestasBasicas = produtoService.montarCesta();
        return ResponseEntity.status(201).body(cestasBasicas);
    }

    @GetMapping("/cesta-basica")
    public ResponseEntity<List<CestaBasicaDTO>> listarCestasBasicas() {
        List<CestaBasicaDTO> cestasBasicas = produtoService.listarCestasBasicas();
        return ResponseEntity.ok(cestasBasicas);
    }


}
