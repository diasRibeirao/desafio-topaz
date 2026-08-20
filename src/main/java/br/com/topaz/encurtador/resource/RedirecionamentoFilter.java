package br.com.topaz.encurtador.resource;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.service.UrlCurtaService;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter("/*")
public class RedirecionamentoFilter implements Filter {

    @Inject
    private UrlCurtaService service;

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
        // Nenhuma inicializacao adicional necessaria.
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        String metodo = request.getMethod();

        if (!"GET".equalsIgnoreCase(metodo)
                && !"HEAD".equalsIgnoreCase(metodo)) {

            chain.doFilter(request, response);
            return;
        }

        String codigo = extrairCodigo(request);

        /*
         * Se nao for uma URL curta, continua o processamento normal.
         * Isso permite que API, frontend e arquivos estaticos continuem
         * sendo atendidos normalmente pelo container.
         */
        if (codigo == null || caminhoReservado(codigo)) {
            chain.doFilter(request, response);
            return;
        }

        Optional<UrlCurta> urlCurta =
                service.buscarPorCodigo(codigo);

        if (!urlCurta.isPresent()) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "URL curta nao encontrada."
            );
            return;
        }

        response.setStatus(
                HttpServletResponse.SC_FOUND
        );

        response.setHeader(
                "Location",
                urlCurta.get().getUrlOriginal()
        );
    }

    @Override
    public void destroy() {
        // Nenhum recurso adicional precisa ser liberado.
    }

    private String extrairCodigo(
            HttpServletRequest request) {

        String contextPath =
                request.getContextPath();

        String uri =
                request.getRequestURI();

        String path =
                uri.substring(contextPath.length());

        if (path.isEmpty() || "/".equals(path)) {
            return null;
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        // Uma URL curta possui somente um segmento.
        if (path.contains("/")) {
            return null;
        }

        return path;
    }

    private boolean caminhoReservado(String codigo) {

        return "api".equalsIgnoreCase(codigo)
                || "index.html".equalsIgnoreCase(codigo)
                || "app.js".equalsIgnoreCase(codigo)
                || "style.css".equalsIgnoreCase(codigo)
                || "favicon.ico".equalsIgnoreCase(codigo);
    }
}