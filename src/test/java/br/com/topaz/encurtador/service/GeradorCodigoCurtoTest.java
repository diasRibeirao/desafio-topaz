package br.com.topaz.encurtador.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeradorCodigoCurtoTest {

    private final GeradorCodigoCurto gerador =
            new GeradorCodigoCurto();

    @Test
    public void deveGerarCodigoComSeteCaracteres() {

        String code = gerador.gerar();

        assertNotNull(code);
        assertEquals(7, code.length());
    }

    @Test
    public void deveGerarApenasCaracteresPermitidos() {

        String code = gerador.gerar();

        assertTrue(
                code.matches("^[A-Za-z0-9]+$")
        );
    }

    @Test
    public void deveGerarCodigoComTamanhoInformado() {

        String code = gerador.gerar(10);

        assertEquals(10, code.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarTamanhoZero() {

        gerador.gerar(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarTamanhoNegativo() {

        gerador.gerar(-1);
    }
}