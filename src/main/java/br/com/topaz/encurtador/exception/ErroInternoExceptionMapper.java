package br.com.topaz.encurtador.exception;

import br.com.topaz.encurtador.dto.ErroResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErroInternoExceptionMapper
        implements ExceptionMapper<Exception> {

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception exception) {

        ErroResponse erro =
                new ErroResponse(
                        500,
                        "Internal Server Error",
                        "Ocorreu um erro inesperado.",
                        request.getRequestURI()
                );

        return Response
                .status(
                        Response.Status.INTERNAL_SERVER_ERROR
                )
                .type(MediaType.APPLICATION_JSON)
                .entity(erro)
                .build();
    }
}