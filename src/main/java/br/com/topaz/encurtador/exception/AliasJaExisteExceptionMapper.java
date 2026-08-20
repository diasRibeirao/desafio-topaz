package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AliasJaExisteExceptionMapper
        implements ExceptionMapper<AliasJaExisteException> {

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(
            AliasJaExisteException exception) {

        ErroResponse erro =
                new ErroResponse(
                        409,
                        "Conflict",
                        exception.getMessage(),
                        request.getRequestURI()
                );

        return Response
                .status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(erro)
                .build();
    }
}