package com.api.jesus_king_tech.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @PostMapping
    public ResponseEntity<String> cadastrarProduto(@RequestBody Produto produto) {
        produtoRepository.save(produto);
        return ResponseEntity.status(201).body("Produto cadastrado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setPeso(produtoAtualizado.getPeso());
            produto.setCategoria(produtoAtualizado.getCategoria());
            produto.setTipo(produtoAtualizado.getTipo());
            produtoRepository.save(produto);
            return ResponseEntity.status(200).body("Produto atualizado com sucesso!");
        }

        return ResponseEntity.status(404).body("Produto não encontrado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Integer id) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);

        if (produtoOpt.isPresent()) {
            produtoRepository.delete(produtoOpt.get());
            return ResponseEntity.status(200).body("Produto removido com sucesso!");
        }

        return ResponseEntity.status(404).body("Produto não encontrado");
    }

    @PostMapping("/categorias")
    public ResponseEntity<String> cadastrarCategoria(@RequestBody Categoria categoria) {
        categoriaRepository.save(categoria);
        return ResponseEntity.status(201).body("Categoria cadastrada com sucesso!");
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<String> atualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaAtualizada) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);

        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setNome(categoriaAtualizada.getNome());
            categoriaRepository.save(categoria);
            return ResponseEntity.status(200).body("Categoria atualizada com sucesso!");
        }

        return ResponseEntity.status(404).body("Categoria não encontrada");
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<String> deletarCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);

        if (categoriaOpt.isPresent()) {
            categoriaRepository.delete(categoriaOpt.get());
            return ResponseEntity.status(200).body("Categoria removida com sucesso!");
        }

        return ResponseEntity.status(404).body("Categoria não encontrada");
    }

    @PostMapping("/tipos")
    public ResponseEntity<String> cadastrarTipo(@RequestBody Tipo tipo) {
        tipoRepository.save(tipo);
        return ResponseEntity.status(201).body("Tipo cadastrado com sucesso!");
    }

    @PutMapping("/tipos/{id}")
    public ResponseEntity<String> atualizarTipo(@PathVariable Integer id, @RequestBody Tipo tipoAtualizado) {
        Optional<Tipo> tipoOpt = tipoRepository.findById(id);

        if (tipoOpt.isPresent()) {
            Tipo tipo = tipoOpt.get();
            tipo.setNome(tipoAtualizado.getNome());
            tipoRepository.save(tipo);
            return ResponseEntity.status(200).body("Tipo atualizado com sucesso!");
        }

        return ResponseEntity.status(404).body("Tipo não encontrado");
    }

    @DeleteMapping("/tipos/{id}")
    public ResponseEntity<String> deletarTipo(@PathVariable Integer id) {
        Optional<Tipo> tipoOpt = tipoRepository.findById(id);

        if (tipoOpt.isPresent()) {
            tipoRepository.delete(tipoOpt.get());
            return ResponseEntity.status(200).body("Tipo removido com sucesso!");
        }

        return ResponseEntity.status(404).body("Tipo não encontrado");
    }

    private ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setPeso(produto.getPeso());
        dto.setDataEntrada(produto.getDataEntrada());

        if (produto.getCategoria() != null) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(produto.getCategoria().getId());
            categoriaDTO.setNome(produto.getCategoria().getNome());
            dto.setCategoria(categoriaDTO);
        }

        if (produto.getTipo() != null) {
            TipoDTO tipoDTO = toDTO(produto.getTipo());
            dto.setTipo(tipoDTO);
        }

        return dto;
    }

    private TipoDTO toDTO(Tipo tipo) {
        TipoDTO dto = new TipoDTO();
        dto.setId(tipo.getId());
        dto.setNome(tipo.getNome());

        if (tipo.getCategoria() != null) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(tipo.getCategoria().getId());
            categoriaDTO.setNome(tipo.getCategoria().getNome());
            dto.setCategoria(categoriaDTO);
        }

        return dto;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        List<ProdutoDTO> produtosDTO = produtoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(produtosDTO);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> listarTodasCategorias() {
        List<CategoriaDTO> categoriasDTO = categoriaRepository.findAll().stream()
                .map(categoria -> {
                    CategoriaDTO dto = new CategoriaDTO();
                    dto.setId(categoria.getId());
                    dto.setNome(categoria.getNome());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoriasDTO);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoDTO>> listarTodosTipos() {
        List<TipoDTO> tiposDTO = tipoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tiposDTO);
    }

}
