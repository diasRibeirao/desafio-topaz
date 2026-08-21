package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidacaoExceptionMapperTest {

    private ValidacaoExceptionMapper mapper;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {

        mapper = new ValidacaoExceptionMapper();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/url-encurtador/api/urls");

        injetarDependencia(
                mapper,
                "request",
                request
        );
    }

    @Test
    public void deveRetornar400ParaErroDeValidacao() {

        Response response =
                mapper.toResponse(
                        new ValidacaoException(
                                "A URL e obrigatoria."
                        )
                );

        assertEquals(
                400,
                response.getStatus()
        );

        ErroResponse erro =
                (ErroResponse) response.getEntity();

        assertNotNull(erro);

        assertEquals(
                400,
                erro.getStatus()
        );

        assertEquals(
                "Bad Request",
                erro.getErro()
        );

        assertEquals(
                "A URL e obrigatoria.",
                erro.getMensagem()
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