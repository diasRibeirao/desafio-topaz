package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ErroInternoExceptionMapperTest {

    private ErroInternoExceptionMapper mapper;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {

        mapper =
                new ErroInternoExceptionMapper();

        request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn(
                        "/url-encurtador/api/urls"
                );

        injetarDependencia(
                mapper,
                "request",
                request
        );
    }

    @Test
    public void deveRetornar500SemExporDetalhesInternos() {

        RuntimeException exception =
                new RuntimeException(
                        "Erro SQL tabela PASSWORD"
                );

        Response response =
                mapper.toResponse(exception);

        assertEquals(
                500,
                response.getStatus()
        );

        ErroResponse erro =
                (ErroResponse) response.getEntity();

        assertNotNull(erro);

        assertEquals(
                500,
                erro.getStatus()
        );

        assertEquals(
                "Internal Server Error",
                erro.getErro()
        );

        assertEquals(
                "Ocorreu um erro inesperado.",
                erro.getMensagem()
        );

        assertFalse(
                erro.getMensagem()
                        .contains("SQL")
        );

        assertFalse(
                erro.getMensagem()
                        .contains("PASSWORD")
        );

        assertNotNull(
                erro.getTimestamp()
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