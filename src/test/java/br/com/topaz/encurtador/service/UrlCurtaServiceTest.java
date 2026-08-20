package br.com.topaz.encurtador.service;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.exception.AliasJaExisteException;
import br.com.topaz.encurtador.exception.ValidacaoException;
import br.com.topaz.encurtador.repository.UrlCurtaRepository;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class UrlCurtaServiceTest {

    private UrlCurtaService service;
    private UrlCurtaRepository repository;
    private GeradorCodigoCurto gerador;

    @Before
    public void setUp() throws Exception {

        service = new UrlCurtaService();
        repository = new UrlCurtaRepository();
        gerador = new GeradorCodigoCurto();

        injetarDependencia(
                service,
                "repository",
                repository
        );

        injetarDependencia(
                service,
                "gerador",
                gerador
        );
    }

    @Test
    public void deveCriarUrlCurtaComAlias() {

        UrlCurta resultado =
                service.criar(
                        "https://www.google.com",
                        "google"
                );

        assertNotNull(resultado);
        assertEquals(
                "google",
                resultado.getCodigo()
        );

        assertEquals(
                "https://www.google.com",
                resultado.getUrlOriginal()
        );
    }

    @Test
    public void deveGerarCodigoQuandoAliasNaoForInformado() {

        UrlCurta resultado =
                service.criar(
                        "https://www.google.com",
                        null
                );

        assertNotNull(resultado);
        assertNotNull(resultado.getCodigo());
        assertEquals(
                7,
                resultado.getCodigo().length()
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarUrlNula() {

        service.criar(
                null,
                null
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarUrlVazia() {

        service.criar(
                "",
                null
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarUrlSemHttpOuHttps() {

        service.criar(
                "www.google.com",
                null
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarUrlInvalida() {

        service.criar(
                "url-invalida",
                null
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarAliasMuitoCurto() {

        service.criar(
                "https://www.google.com",
                "ab"
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarAliasComEspaco() {

        service.criar(
                "https://www.google.com",
                "meu link"
        );
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarAliasComCaracterEspecial() {

        service.criar(
                "https://www.google.com",
                "google@topaz"
        );
    }

    @Test(expected = AliasJaExisteException.class)
    public void deveRejeitarAliasDuplicado() {

        service.criar(
                "https://www.google.com",
                "google"
        );

        service.criar(
                "https://www.topazevolution.com",
                "google"
        );
    }

    @Test
    public void deveAceitarAliasComHifen() {

        UrlCurta resultado =
                service.criar(
                        "https://www.google.com",
                        "meu-link"
                );

        assertEquals(
                "meu-link",
                resultado.getCodigo()
        );
    }

    @Test
    public void deveAceitarAliasComUnderline() {

        UrlCurta resultado =
                service.criar(
                        "https://www.google.com",
                        "meu_link"
                );

        assertEquals(
                "meu_link",
                resultado.getCodigo()
        );
    }

    private void injetarDependencia(
            Object target,
            String nomeCampo,
            Object valor) throws Exception {

        Field field =
                target.getClass()
                        .getDeclaredField(nomeCampo);

        field.setAccessible(true);
        field.set(target, valor);
    }
}