package com.api.jesus_king_tech.domain.endereco.dto;

import com.api.jesus_king_tech.domain.endereco.Endereco;

import java.util.*;
import java.util.stream.Collectors;

public class ListaEstaticaEnderecoResponse {



    private EnderecoResponse[] vetor;


    private int nroElem;


    public ListaEstaticaEnderecoResponse(int tam) {
        this.vetor = new EnderecoResponse[tam];
        this.nroElem = 0;
    }


    public void adiciona(EnderecoResponse endereco) {
        if (nroElem >= vetor.length) {
            throw new IllegalStateException("A lista está cheia!");
        }
        vetor[nroElem++] = endereco;
    }


    public Optional<EnderecoResponse> findById(int id) {
        if (id < 0 || id >= nroElem) {
            return Optional.empty();
        }
        return Optional.ofNullable(vetor[id]);
    }




    public List<EnderecoResponse> exibir() {
        List<EnderecoResponse> enderecosVisiveis = new ArrayList<>();
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i] != null) {
                enderecosVisiveis.add(vetor[i]);
            }
        }
        return enderecosVisiveis;
    }




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


    public static void main(String[] args) {


    }

    //teste para subir uma branch na CI


}


