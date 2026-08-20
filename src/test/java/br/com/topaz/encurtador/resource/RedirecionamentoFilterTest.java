package br.com.topaz.encurtador.resource;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.service.UrlCurtaService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class RedirecionamentoFilterTest {

    private RedirecionamentoFilter filter;
    private UrlCurtaService service;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @Before
    public void setUp() throws Exception {

        filter = new RedirecionamentoFilter();

        service = mock(UrlCurtaService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        injetarDependencia(
                filter,
                "service",
                service
        );
    }

    @Test
    public void deveRedirecionarQuandoCodigoExistir()
            throws Exception {

        UrlCurta urlCurta =
                new UrlCurta(
                        "google",
                        "https://www.google.com"
                );

        when(request.getMethod())
                .thenReturn("GET");

        when(request.getContextPath())
                .thenReturn("/url-encurtador");

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/google"
                );

        when(service.buscarPorCodigo("google"))
                .thenReturn(
                        Optional.of(urlCurta)
                );

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(response).setStatus(
                HttpServletResponse.SC_FOUND
        );

        verify(response).setHeader(
                "Location",
                "https://www.google.com"
        );

        verify(chain, never())
                .doFilter(request, response);
    }

    @Test
    public void deveRetornar404QuandoCodigoNaoExistir()
            throws Exception {

        when(request.getMethod())
                .thenReturn("GET");

        when(request.getContextPath())
                .thenReturn("/url-encurtador");

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/inexistente"
                );

        when(service.buscarPorCodigo("inexistente"))
                .thenReturn(Optional.empty());

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(response).sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "URL curta nao encontrada."
        );
    }

    @Test
    public void devePermitirRequisicaoPostContinuar()
            throws Exception {

        when(request.getMethod())
                .thenReturn("POST");

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(chain).doFilter(
                request,
                response
        );

        verifyZeroInteractions(service);
    }

    @Test
    public void devePermitirAcessoAoFrontend()
            throws Exception {

        when(request.getMethod())
                .thenReturn("GET");

        when(request.getContextPath())
                .thenReturn("/url-encurtador");

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/index.html"
                );

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(chain).doFilter(
                request,
                response
        );

        verifyZeroInteractions(service);
    }

    @Test
    public void devePermitirAcessoAApi()
            throws Exception {

        when(request.getMethod())
                .thenReturn("GET");

        when(request.getContextPath())
                .thenReturn("/url-encurtador");

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/api/urls"
                );

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(chain).doFilter(
                request,
                response
        );

        verifyZeroInteractions(service);
    }

    @Test
    public void deveRedirecionarRequisicaoHead()
            throws Exception {

        UrlCurta urlCurta =
                new UrlCurta(
                        "google",
                        "https://www.google.com"
                );

        when(request.getMethod())
                .thenReturn("HEAD");

        when(request.getContextPath())
                .thenReturn("/url-encurtador");

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/google"
                );

        when(service.buscarPorCodigo("google"))
                .thenReturn(
                        Optional.of(urlCurta)
                );

        filter.doFilter(
                request,
                response,
                chain
        );

        verify(response).setStatus(
                HttpServletResponse.SC_FOUND
        );

        verify(response).setHeader(
                "Location",
                "https://www.google.com"
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