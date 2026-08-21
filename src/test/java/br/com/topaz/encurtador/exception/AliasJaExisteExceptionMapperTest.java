package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AliasJaExisteExceptionMapperTest {

    private AliasJaExisteExceptionMapper mapper;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {

        mapper =
                new AliasJaExisteExceptionMapper();

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
    public void deveRetornar409QuandoAliasJaExistir() {

        Response response =
                mapper.toResponse(
                        new AliasJaExisteException(
                                "google"
                        )
                );

        assertEquals(
                409,
                response.getStatus()
        );

        ErroResponse erro =
                (ErroResponse) response.getEntity();

        assertNotNull(erro);

        assertEquals(
                409,
                erro.getStatus()
        );

        assertEquals(
                "Conflict",
                erro.getErro()
        );

        assertTrue(
                erro.getMensagem()
                        .contains("google")
        );

        assertEquals(
                "/url-encurtador/api/urls",
                erro.getCaminho()
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