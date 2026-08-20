package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidacaoExceptionMapper
        implements ExceptionMapper<ValidacaoException> {

    @Override
    public Response toResponse(ValidacaoException exception) {

        ErroResponse erro = new ErroResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(erro)
                .build();
    }
}