package com.api.jesus_king_tech.ordenacao_viacep;

import org.springframework.web.client.RestTemplate;

public class EnderecoController {

    public Endereco buscarEnderecoPorCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, Endereco.class);
    }

    public void ordenarPorLogradouro(Endereco[] enderecos) {
        for (int i = 0; i < enderecos.length - 1; i++) {
            int indiceMinimo = i;

            for (int j = i + 1; j < enderecos.length; j++) {
                if (enderecos[j].getLogradouro().compareTo(enderecos[indiceMinimo].getLogradouro()) < 0) {
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

    public Endereco[] buscarEOrdenarEnderecosPorLogradouro(String[] ceps) {
        Endereco[] enderecos = new Endereco[ceps.length];
        for (int i = 0; i < ceps.length; i++) {
            enderecos[i] = buscarEnderecoPorCep(ceps[i]);
        }
        ordenarPorLogradouro(enderecos);

        return enderecos;
    }

    public static void main(String[] args) {
        EnderecoController encontrar = new EnderecoController();

        String[] ceps = {"02879130", "01002000", "01003000",
                "06010000",
                "07020000",
                "01414000",
                "05001000",
                "02011000",
                "01001000"};

        Endereco[] enderecosOrdenados = encontrar.buscarEOrdenarEnderecosPorLogradouro(ceps);

        for (Endereco endereco : enderecosOrdenados) {
            if (endereco != null) {
                System.out.println(endereco.getLogradouro() + ", " + endereco.getBairro() + ", " + endereco.getLocalidade() + ", " + endereco.getUf());
            } else {
                System.out.println("Endereço não encontrado");
            }
        }
    }
    }






