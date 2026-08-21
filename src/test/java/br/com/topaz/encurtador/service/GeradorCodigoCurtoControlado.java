package br.com.topaz.encurtador.service;

import java.util.LinkedList;
import java.util.Queue;

public class GeradorCodigoCurtoControlado
        extends GeradorCodigoCurto {

    private final Queue<String> codigos =
            new LinkedList<String>();

    public GeradorCodigoCurtoControlado(
            String... codigos) {

        for (String codigo : codigos) {
            this.codigos.add(codigo);
        }
    }

    @Override
    public String gerar() {

        if (codigos.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum codigo configurado para o teste."
            );
        }

        return codigos.remove();
    }
}