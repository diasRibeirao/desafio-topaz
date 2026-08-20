package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AliasJaExisteExceptionMapper
        implements ExceptionMapper<AliasJaExisteException> {

    @Override
    public Response toResponse(AliasJaExisteException exception) {

        ErroResponse erro = new ErroResponse(
                409,
                "Conflict",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(erro)
                .build();
    }
}