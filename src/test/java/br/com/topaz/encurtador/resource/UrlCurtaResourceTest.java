package br.com.topaz.encurtador.resource;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.dto.CriarUrlCurtaRequest;
import br.com.topaz.encurtador.dto.CriarUrlCurtaResponse;
import br.com.topaz.encurtador.service.UrlCurtaService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UrlCurtaResourceTest {

    private UrlCurtaResource resource;
    private UrlCurtaService service;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {

        resource =
                new UrlCurtaResource();

        service =
                mock(UrlCurtaService.class);

        request =
                mock(HttpServletRequest.class);

        injetarDependencia(
                resource,
                "service",
                service
        );

        injetarDependencia(
                resource,
                "request",
                request
        );
    }

    @Test
    public void deveRetornar201AoCriarUrlCurta() {

        CriarUrlCurtaRequest entrada =
                new CriarUrlCurtaRequest();

        entrada.setUrl(
                "https://www.google.com"
        );

        entrada.setAlias(
                "google"
        );

        UrlCurta urlCurta =
                new UrlCurta(
                        "google",
                        "https://www.google.com"
                );

        when(
                service.criar(
                        "https://www.google.com",
                        "google"
                )
        ).thenReturn(urlCurta);

        when(request.getScheme())
                .thenReturn("http");

        when(request.getServerName())
                .thenReturn("localhost");

        when(request.getServerPort())
                .thenReturn(8080);

        when(request.getContextPath())
                .thenReturn(
                        "/url-encurtador"
                );

        Response response =
                resource.create(entrada);

        assertEquals(
                201,
                response.getStatus()
        );

        CriarUrlCurtaResponse body =
                (CriarUrlCurtaResponse)
                        response.getEntity();

        assertNotNull(body);

        assertEquals(
                "google",
                body.getCodigo()
        );

        assertEquals(
                "https://www.google.com",
                body.getUrlOriginal()
        );

        assertEquals(
                "http://localhost:8080/url-encurtador/google",
                body.getUrlCurta()
        );

        verify(service).criar(
                "https://www.google.com",
                "google"
        );
    }

    private void injetarDependencia(
            Object target,
            String campo,
            Object valor)
            throws Exception {

        Field field =
                target.getClass()
                        .getDeclaredField(campo);

        field.setAccessible(true);
        field.set(target, valor);
    }
}