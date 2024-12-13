package com.api.jesus_king_tech.Service;


import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.repository.ProdutoRepository;
import com.api.jesus_king_tech.exception.ExceptionHttp;
import com.api.jesus_king_tech.service.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Test
    @DisplayName("Cadastrar Produto deve salvar e retornar DTO corretamente")
    void cadastrarProduto() {
        Produto produto = new Produto();
        produto.setPeso_litro(10.5);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1);
        produtoSalvo.setPeso_litro(10.5);

        ProdutoMapper produtoMapperReal = new ProdutoMapper();
        ProdutoDTO produtoEsperado = produtoMapperReal.toProdutoDTO(produtoSalvo);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produtoSalvo);

        ProdutoDTO resultado = produtoService.cadastrarProduto(produto);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(produtoEsperado.getId(), resultado.getId());
        Assertions.assertEquals(produtoEsperado.getPeso(), resultado.getPeso());
    }

    @Test
    @DisplayName("Listar Todos deve retornar lista de Produtos")
    void listarTodos() {
        List<Produto> produtos = List.of(new Produto(), new Produto());

        Mockito.when(produtoRepository.findAll()).thenReturn(produtos);

        List<ProdutoDTO> resultado = produtoService.listarTodos();

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Atualizar Produto deve atualizar e retornar DTO")
    void atualizarProduto() {
        Integer id = 1;
        Produto produtoExistente = new Produto();
        produtoExistente.setId(id);
        produtoExistente.setPeso_litro(10.5);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setPeso_litro(12.0);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(id);
        produtoSalvo.setPeso_litro(12.0);

        ProdutoMapper produtoMapperReal = new ProdutoMapper();
        ProdutoDTO produtoEsperado = produtoMapperReal.toProdutoDTO(produtoSalvo);

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        Mockito.when(produtoRepository.save(produtoExistente)).thenReturn(produtoSalvo);

        ProdutoDTO resultado = produtoService.atualizarProduto(id, produtoAtualizado);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(produtoEsperado.getId(), resultado.getId());
        Assertions.assertEquals(produtoEsperado.getPeso(), resultado.getPeso());
    }

    @Test
    @DisplayName("Atualizar Produto deve retornar null se Produto não encontrado")
    void atualizarProdutoNaoEncontrado() {
        Integer id = 1;
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setPeso_litro(12.0);

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        ProdutoDTO resultado = produtoService.atualizarProduto(id, produtoAtualizado);

        Assertions.assertNull(resultado);
    }

    @Test
    @DisplayName("Deletar Produto deve retornar true se Produto existir")
    void deletarProduto() {
        Integer id = 1;
        Produto produto = new Produto();
        produto.setId(id);

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        boolean resultado = produtoService.deletarProduto(id);

        Assertions.assertTrue(resultado);
        Mockito.verify(produtoRepository, Mockito.times(1)).delete(produto);
    }

    @Test
    @DisplayName("Deletar Produto deve retornar false se Produto não encontrado")
    void deletarProdutoNaoEncontrado() {
        Integer id = 1;

        Mockito.when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        boolean resultado = produtoService.deletarProduto(id);

        Assertions.assertFalse(resultado);
        Mockito.verify(produtoRepository, Mockito.never()).delete(any(Produto.class));
    }


    @Test
    @DisplayName("Listar todos deve retornar lista vazia quando não houver produtos")
    void listarTodosProdutosVazia() {
        Mockito.when(produtoRepository.findAll()).thenReturn(List.of());

        List<ProdutoDTO> resultado = produtoService.listarTodos();

        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());
    }


    @Test
    @DisplayName("Atualizar Produto deve retornar null se ID não for encontrado")
    void atualizarProdutoIdNaoEncontrado() {
        Integer idInexistente = 999;
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setPeso_litro(15.0);

        Mockito.when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ProdutoDTO resultado = produtoService.atualizarProduto(idInexistente, produtoAtualizado);

        Assertions.assertNull(resultado);
        Mockito.verify(produtoRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    @DisplayName("Deletar Produto deve retornar false se ID não for encontrado")
    void deletarProdutoIdNaoEncontrado() {
        Integer idInexistente = 999;

        Mockito.when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        boolean resultado = produtoService.deletarProduto(idInexistente);

        Assertions.assertFalse(resultado);
        Mockito.verify(produtoRepository, Mockito.never()).delete(Mockito.any());
    }






}