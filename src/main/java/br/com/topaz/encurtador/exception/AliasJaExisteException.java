package br.com.topaz.encurtador.exception;

public class AliasJaExisteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AliasJaExisteException(String alias) {
        super("O alias '" + alias + "' ja esta em uso.");
    }
}