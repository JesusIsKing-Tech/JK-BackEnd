package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.repository.ProdutoRepository;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import com.api.jesus_king_tech.service.CategoriaService;
import com.api.jesus_king_tech.service.ProdutoService;
import com.api.jesus_king_tech.service.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService, TipoService tipoService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.tipoService = tipoService;
    }

    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody Produto produto) {
        ProdutoDTO produtoDTO = produtoService.cadastrarProduto(produto);
        return ResponseEntity.status(201).body(produtoDTO);
    }


    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

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


}
