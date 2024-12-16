package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.cesta_basica.CestaBasica;
import com.api.jesus_king_tech.domain.cesta_basica.dto.CestaBasicaDTO;
import com.api.jesus_king_tech.domain.cesta_basica.repository.CestaBasicaRepository;
import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.repository.CategoriaRepository;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.repository.ProdutoRepository;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.repository.TipoRepository;
import com.api.jesus_king_tech.exception.ResourceNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private CestaBasicaRepository cestaBasicaRepository;

    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
        this.produtoRepository = produtoRepository;
        this.produtoMapper = new ProdutoMapper();
        this.categoriaRepository = categoriaRepository;
    }


    public ProdutoDTO cadastrarProduto(Produto produto) {

        if (produto.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }

        // Buscar a categoria pelo ID
        Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        produto.setCategoria(categoria);

        if (produto.getTipo() == null) {
            throw new IllegalArgumentException("Tipo não pode ser nulo");
        }

        // Buscar o tipo pelo ID
        Tipo tipo = tipoRepository.findById(produto.getTipo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo não encontrado"));
        produto.setTipo(tipo);

        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toProdutoDTO(produtoSalvo);
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .filter(produto -> produto.getCestaBasica() == null)
                .map(produtoMapper::toProdutoDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO atualizarProduto(Integer id, Produto produtoAtualizado) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setPeso_litro(produtoAtualizado.getPeso_litro());
            produto.setCategoria(produtoAtualizado.getCategoria());
//            produto.setTipo(produtoAtualizado.getTipo());
            produtoRepository.save(produto);
            return produtoMapper.toProdutoDTO(produto);
        }
        return null;
    }

    public boolean deletarProduto(Integer id) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            produtoRepository.delete(produtoOpt.get());
            return true;
        }
        return false;
    }

    public List<CestaBasicaDTO> montarCesta() throws BadRequestException {
        // Definindo os requisitos mínimos para montar uma cesta básica
        Map<String, Double> requisitos = new HashMap<>();
        requisitos.put("Arroz", 7.0);
        requisitos.put("Feijão", 5.0);
        requisitos.put("Macarrão", 2.0);
        requisitos.put("Farinha", 2.0);
        requisitos.put("Óleo", 2.0);
        requisitos.put("Açúcar", 2.0);
        requisitos.put("Sal", 2.0);
        requisitos.put("Café", 1.0);
        requisitos.put("Leite em pó", 2.0);

        List<CestaBasica> cestasBasicas = new ArrayList<>();

        while (true) {
            // Verificar se há produtos suficientes para montar uma cesta básica
            if (!temProdutosSuficientes(requisitos)) {
                break;
            }

            // Criar uma nova cesta básica
            CestaBasica cestaBasica1 = new CestaBasica();
            cestaBasica1.setDataMontagem(LocalDate.now());
            cestaBasica1.setProdutos(new ArrayList<>());


            // Salvar a cesta básica antes de associar os produtos
            CestaBasica cestaBasica = cestaBasicaRepository.save(cestaBasica1);

            System.out.println(cestaBasica.getId());
            System.out.println(cestaBasica.toString());
            if (cestaBasica == null) {
                throw new BadRequestException("Não foi possível criar a cesta básica");
            }



            // Adicionar produtos à cesta básica e atualizar o estoque
            for (Map.Entry<String, Double> requisito : requisitos.entrySet()) {
                String nomeProduto = requisito.getKey();
                Double quantidadeNecessaria = requisito.getValue();

                List<Produto> produtos = produtoRepository.findAllByCategoriaNome(nomeProduto)
                        .stream()
                        .filter(produto -> produto.getCestaBasica() == null)
                        .toList();

                double quantidadeTotal = 0.0;

                for (Produto produto : produtos) {
                    if (quantidadeTotal >= quantidadeNecessaria) {
                        break;
                    }
                    double quantidadeAdicionar = Math.min(produto.getPeso_litro(), quantidadeNecessaria - quantidadeTotal);
                    quantidadeTotal += quantidadeAdicionar;
                    produto.setCestaBasica(cestaBasica);
                    cestaBasica.getProdutos().add(produto);
                    produtoRepository.save(produto);
                }
            }

            // Salvar a cesta básica
            cestasBasicas.add(cestaBasica);
            cestaBasicaRepository.save(cestaBasica);
        }

        if (cestasBasicas.isEmpty()) {
            throw new ResourceNotFoundException("Não há produtos suficientes para montar uma cesta básica");
        }

        return  cestasBasicas.stream()
                .map(produtoMapper::toCestaBasicaDTO)
                .collect(Collectors.toList());
    }

    private boolean temProdutosSuficientes(Map<String, Double> requisitos) {
        for (Map.Entry<String, Double> requisito : requisitos.entrySet()) {
            String nomeProduto = requisito.getKey();
            Double quantidadeNecessaria = requisito.getValue();

            List<Produto> produtos = produtoRepository.findAllByCategoriaNome(nomeProduto);

            if (produtos == null || produtos.isEmpty()){
                return false;
            }

            double quantidadeTotal = produtos.stream()
                    .filter(produto -> produto.getCestaBasica() == null)
                    .mapToDouble(produto -> produto.getPeso_litro() != null ? produto.getPeso_litro() : 0)
                    .sum();

            if (quantidadeTotal < quantidadeNecessaria) {
                return false;
            }
        }
        return true;
    }

    public List<CestaBasicaDTO> listarCestasBasicas() {
        return cestaBasicaRepository.findAll().stream()
                .map(produtoMapper::toCestaBasicaDTO)
                .collect(Collectors.toList());
    }

    public void doarCesta() {

        // Definindo os requisitos mínimos para montar uma cesta básica
        Map<String, Double> requisitos = new HashMap<>();
        requisitos.put("Arroz", 7.0);
        requisitos.put("Feijão", 5.0);
        requisitos.put("Macarrão", 2.0);
        requisitos.put("Farinha", 2.0);
        requisitos.put("Óleo", 2.0);
        requisitos.put("Açúcar", 2.0);
        requisitos.put("Sal", 2.0);
        requisitos.put("Café", 1.0);
        requisitos.put("Leite em pó", 2.0);

        if (!temProdutosSuficientes(requisitos)) {
            throw new ResourceNotFoundException("Não há produtos suficientes para montar uma cesta básica");
        }

        // Adicionar produtos à cesta básica e deletar do estoque
        for (Map.Entry<String, Double> requisito : requisitos.entrySet()) {
            String nomeProduto = requisito.getKey();
            Double quantidadeNecessaria = requisito.getValue();

            List<Produto> produtos = produtoRepository.findAllByCategoriaNome(nomeProduto)
                    .stream()
                    .filter(produto -> produto.getCestaBasica() == null)
                    .toList();

            double quantidadeTotal = 0.0;

            for (Produto produto : produtos) {
                if (quantidadeTotal >= quantidadeNecessaria) {
                    break;
                }
                double quantidadeAdicionar = Math.min(produto.getPeso_litro(), quantidadeNecessaria - quantidadeTotal);
                quantidadeTotal += quantidadeAdicionar;
                produtoRepository.delete(produto);
            }
        }
    }
}