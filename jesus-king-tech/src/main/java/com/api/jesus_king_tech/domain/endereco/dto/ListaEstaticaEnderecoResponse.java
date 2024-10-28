package com.api.jesus_king_tech.domain.endereco.dto;

import com.api.jesus_king_tech.domain.endereco.Endereco;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListaEstaticaEnderecoResponse {


    // Vetor para armazenar os endereços
    private EnderecoResponse[] vetor;

    // Número de elementos no vetor
    private int nroElem;

    // Construtor - inicializa o vetor com o tamanho máximo
    public ListaEstaticaEnderecoResponse(int tam) {
        this.vetor = new EnderecoResponse[tam];
        this.nroElem = 0;
    }

    // Adiciona um endereço na lista
    public void adiciona(EnderecoResponse endereco) {
        if (nroElem >= vetor.length) {
            throw new IllegalStateException("A lista está cheia!");
        }
        vetor[nroElem++] = endereco;
    }

    // Busca um endereço pelo índice (id)
    public Optional<EnderecoResponse> findById(int id) {
        if (id < 0 || id >= nroElem) { // Verifica se id está dentro dos limites do array
            return Optional.empty();
        }
        return Optional.ofNullable(vetor[id]);
    }

    // Busca um endereço pelo logradouro
    public int busca(String logradouro) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].getLogradouro().equals(logradouro)) {
                return i;
            }
        }
        return -1;
    }

    public List<EnderecoResponse> toList() {
        return Arrays.stream(vetor, 0, nroElem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }



    // Método para verificar se um índice existe
    public boolean existsById(int id) {
        return id >= 0 && id < nroElem && vetor[id] != null;
    }

    // Remove um endereço pelo índice
    public boolean removePeloIndice(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return false;
        }
        for (int i = indice; i < nroElem - 1; i++) {
            vetor[i] = vetor[i + 1];
        }
        nroElem--;
        return true;
    }

    // Remove um endereço pelo logradouro
    public boolean removeEndereco(String logradouro) {
        int indice = busca(logradouro);
        return indice >= 0 && removePeloIndice(indice);
    }

    // Exibe os endereços na lista
    public void exibe() {
        if (nroElem == 0) {
            System.out.println("A lista está vazia.");
        } else {
            for (int i = 0; i < nroElem; i++) {
                EnderecoResponse endereco = vetor[i];
                if (endereco != null) {
                    System.out.printf("CEP: %s, Logradouro: %s, Bairro: %s, Localidade: %s, UF: %s\n",
                            endereco.getCep(), endereco.getLogradouro(), endereco.getBairro(),
                            endereco.getLocalidade(), endereco.getUf());
                }
            }
        }
    }

    public static void quickSort(List<EnderecoResponse> enderecos, int inicio, int fim) {
        if (inicio < fim) {
            int indicePivo = partition(enderecos, inicio, fim);
            quickSort(enderecos, inicio, indicePivo - 1);
            quickSort(enderecos, indicePivo + 1, fim);
        }
    }

    private static int partition(List<EnderecoResponse> enderecos, int inicio, int fim) {
        EnderecoResponse pivo = enderecos.get(fim);
        int i = (inicio - 1);

        for (int j = inicio; j < fim; j++) {
            if (enderecos.get(j).getLogradouro().compareTo(pivo.getLogradouro()) <= 0) {
                i++;
                EnderecoResponse temp = enderecos.get(i);
                enderecos.set(i, enderecos.get(j));
                enderecos.set(j, temp);
            }
        }

        EnderecoResponse temp = enderecos.get(i + 1);
        enderecos.set(i + 1, enderecos.get(fim));
        enderecos.set(fim, temp);

        return i + 1;
    }

    // Métodos auxiliares para obter o número de elementos e o vetor (útil para testes)
    public int getNroElem() {
        return nroElem;
    }

    public EnderecoResponse[] getVetor() {
        return vetor;
    }
}


