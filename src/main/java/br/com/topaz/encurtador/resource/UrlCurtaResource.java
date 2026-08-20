package br.com.topaz.encurtador.resource;


import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.dto.CriarUrlCurtaRequest;
import br.com.topaz.encurtador.dto.CriarUrlCurtaResponse;
import br.com.topaz.encurtador.service.UrlCurtaService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/urls")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UrlCurtaResource {

    @Inject
    private UrlCurtaService service;

    @Context
    private HttpServletRequest request;

    @POST
    public Response create(CriarUrlCurtaRequest input) {

        UrlCurta urlCurta =
                service.criar(
                        input.getUrl(),
                        input.getAlias()
                );

        String valorUrlCurta =
                buildUrlCurta(urlCurta.getCodigo());

        CriarUrlCurtaResponse response =
                new CriarUrlCurtaResponse(
                        valorUrlCurta,
                        urlCurta.getCodigo(),
                        urlCurta.getUrlOriginal()
                );

        return Response
                .status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    private String buildUrlCurta(String code) {

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder baseUrl = new StringBuilder();

        baseUrl.append(scheme)
                .append("://")
                .append(serverName);

        if (!isDefaultPort(scheme, serverPort)) {
            baseUrl.append(":").append(serverPort);
        }

        baseUrl.append(contextPath)
                .append("/")
                .append(code);

        return baseUrl.toString();
    }

    private boolean isDefaultPort(
            String scheme,
            int port) {

        return ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
    }
}