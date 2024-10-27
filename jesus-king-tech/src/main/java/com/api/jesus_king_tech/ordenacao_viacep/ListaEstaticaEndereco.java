package com.api.jesus_king_tech.ordenacao_viacep;

public class ListaEstaticaEndereco {

    // 01) Vetor para armazenar os endereços
    private Endereco[] vetor;

    // 02) Número de elementos no vetor
    private int nroElem;

    // 03) Construtor - inicializa o vetor com o tamanho máximo
    public ListaEstaticaEndereco(int tam) {
        this.vetor = new Endereco[tam];
        this.nroElem = 0;
    }

    // 04) Adiciona um endereço na lista
    public void adiciona(Endereco endereco) {
        if (nroElem >= vetor.length) {
            throw new IllegalStateException("A lista está cheia!");
        }
        vetor[nroElem++] = endereco;
    }

    // 05) Busca um endereço pelo logradouro
    public int busca(String logradouro) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].getLogradouro().equals(logradouro)) {
                return i;
            }
        }
        return -1;
    }

    // 06) Remove um endereço pelo índice
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

    // 07) Remove um endereço pelo logradouro
    public boolean removeEndereco(String logradouro) {
        return removePeloIndice(busca(logradouro));
    }

    // 08) Exibe os endereços na lista
    public void exibe() {
        if (nroElem == 0) {
            System.out.println("A lista está vazia.");
        } else {
            for (int i = 0; i < nroElem; i++) {
                Endereco endereco = vetor[i];
                if (endereco != null) {
                    System.out.printf("%s, %s, %s, %s\n", endereco.getLogradouro(), endereco.getBairro(), endereco.getLocalidade(), endereco.getUf());
                }
            }
        }
    }

    // Métodos auxiliares para obter o número de elementos e o vetor (útil para testes)
    public int getNroElem() {
        return nroElem;
    }

    public Endereco[] getVetor() {
        return vetor;
    }
}

