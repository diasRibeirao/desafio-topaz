package br.com.topaz.encurtador.service;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.exception.AliasJaExisteException;
import br.com.topaz.encurtador.exception.ValidacaoException;
import br.com.topaz.encurtador.repository.UrlCurtaRepository;
import br.com.topaz.encurtador.repository.UrlCurtaRepositoryEmMemoria;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class UrlCurtaServiceTest {

    private UrlCurtaService service;
    private UrlCurtaRepository repository;
    private GeradorCodigoCurto gerador;

    @Before
    public void setUp() throws Exception {

        service = new UrlCurtaService();
        repository =
                new UrlCurtaRepositoryEmMemoria();
        gerador =
                new GeradorCodigoCurto();

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

        service.criar(null, null);
    }

    @Test(expected = ValidacaoException.class)
    public void deveRejeitarUrlVazia() {

        service.criar("", null);
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

    @Test
    public void deveProcessarCriacoesConcorrentesSemPerderUrls()
            throws Exception {

        final int quantidade = 20;

        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        try {

            List<Future<UrlCurta>> resultados =
                    new ArrayList<Future<UrlCurta>>();

            for (int i = 0; i < quantidade; i++) {

                final int indice = i;

                resultados.add(
                        executor.submit(
                                new Callable<UrlCurta>() {

                                    @Override
                                    public UrlCurta call() {

                                        return service.criar(
                                                "https://exemplo.com/" + indice,
                                                null
                                        );
                                    }
                                }
                        )
                );
            }

            Set<String> codigos =
                    new HashSet<String>();

            for (Future<UrlCurta> resultado : resultados) {

                UrlCurta urlCurta =
                        resultado.get();

                assertNotNull(urlCurta);
                assertNotNull(
                        urlCurta.getCodigo()
                );

                codigos.add(
                        urlCurta.getCodigo()
                );
            }

            assertEquals(
                    quantidade,
                    codigos.size()
            );

        } finally {

            executor.shutdown();
        }
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