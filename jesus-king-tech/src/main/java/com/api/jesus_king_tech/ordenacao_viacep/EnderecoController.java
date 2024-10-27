package com.api.jesus_king_tech.ordenacao_viacep;

import org.springframework.web.client.RestTemplate;

public class EnderecoController {

    // 01) Método para buscar endereços e adicioná-los à lista estática
    public ListaEstaticaEndereco buscarEnderecos(String[] ceps, int tamanhoMaximo) {
        ListaEstaticaEndereco listaEnderecos = new ListaEstaticaEndereco(tamanhoMaximo);

        for (String cep : ceps) {
            Endereco endereco = buscarEnderecoPorCep(cep);
            if (endereco != null) {
                listaEnderecos.adiciona(endereco);
            }
        }

        return listaEnderecos;
    }

    // 02) Método para ordenar e exibir os endereços
    public void exibirEnderecosOrdenadosPorLogradouro(String[] ceps) {
        ListaEstaticaEndereco listaEnderecos = buscarEnderecos(ceps, ceps.length);

        // Agora você pode ordenar o array de endereços
        ordenarPorLogradouro(listaEnderecos.getVetor());

        // Exibe a lista ordenada
        listaEnderecos.exibe();
    }

    // Método de busca por CEP (mesmo que você já tem)
    public Endereco buscarEnderecoPorCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, Endereco.class);
    }

    // Método de ordenação (você já tem esse método, apenas reutilizando aqui)
    public void ordenarPorLogradouro(Endereco[] enderecos) {
        for (int i = 0; i < enderecos.length - 1; i++) {
            int indiceMinimo = i;

            for (int j = i + 1; j < enderecos.length; j++) {
                String logradouroAtual = enderecos[j].getLogradouro();
                String logradouroMinimo = enderecos[indiceMinimo].getLogradouro();

                if (logradouroAtual != null && (logradouroMinimo == null || logradouroAtual.compareTo(logradouroMinimo) < 0)) {
                    indiceMinimo = j;
                }
            }
            if (indiceMinimo != i) {
                Endereco aux = enderecos[i];
                enderecos[i] = enderecos[indiceMinimo];
                enderecos[indiceMinimo] = aux;
            }
        }
    }

    // Pesquisa binária recursiva para encontrar o índice de um logradouro
    public int pesquisaBinaria(Endereco[] enderecos, String logradouro, int inicio, int fim) {
        if (inicio > fim) {
            return -1; // Logradouro não encontrado
        }
        int meio = (inicio + fim) / 2;
        String logradouroAtual = enderecos[meio].getLogradouro();

        if (logradouroAtual != null && logradouroAtual.equalsIgnoreCase(logradouro)) {
            return meio; // Logradouro encontrado
        } else if (logradouroAtual != null && logradouroAtual.compareToIgnoreCase(logradouro) > 0) {
            return pesquisaBinaria(enderecos, logradouro, inicio, meio - 1); // Pesquisa à esquerda
        } else {
            return pesquisaBinaria(enderecos, logradouro, meio + 1, fim); // Pesquisa à direita
        }
    }

    // Exemplo de recursão: cálculo do fatorial
    public int fatorial(int n) {
        if (n == 0) {
            return 1; // Caso base
        } else {
            return n * fatorial(n - 1); // Passo recursivo
        }
    }

    public static void main(String[] args) {
        EnderecoController controller = new EnderecoController();

        // Lista de CEPs para buscar endereços
        String[] ceps = {
                "02879-130", "01002000", "01003000",
                "06010000", "07020000", "01414000",
                "05001000", "02011000", "01001000"
        };

        // Definir o tamanho máximo da lista de endereços
        int tamanhoMaximo = ceps.length;

        // Buscar e ordenar os endereços
        ListaEstaticaEndereco listaEnderecos = controller.buscarEnderecos(ceps, tamanhoMaximo);

        // Ordenar os endereços pelo logradouro
        controller.ordenarPorLogradouro(listaEnderecos.getVetor());

        // Imprimir os endereços ordenados
        System.out.println("Endereços ordenados:");
        for (Endereco endereco : listaEnderecos.getVetor()) {
            if (endereco != null && endereco.getLogradouro() != null) {
                System.out.println(endereco.getLogradouro() + ", " + endereco.getBairro() + ", " + endereco.getLocalidade() + ", " + endereco.getUf());
            } else {
                System.out.println("Endereço incompleto ou não encontrado.");
            }
        }

        // Pesquisa binária de um logradouro
        String logradouroBusca = "Praça da Sé";
        int indice = controller.pesquisaBinaria(listaEnderecos.getVetor(), logradouroBusca, 0, listaEnderecos.getNroElem() - 1);
        if (indice != -1) {
            System.out.println("Logradouro '" + logradouroBusca + "' encontrado no índice: " + indice);
        } else {
            System.out.println("Logradouro '" + logradouroBusca + "' não encontrado.");
        }

        // Exemplo de recursão: cálculo do fatorial
        int numero = 5;
        System.out.println("Fatorial de " + numero + " é: " + controller.fatorial(numero));
    }
}
