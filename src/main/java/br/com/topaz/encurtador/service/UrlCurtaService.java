package br.com.topaz.encurtador.service;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.repository.UrlCurtaRepository;
import br.com.topaz.encurtador.exception.AliasJaExisteException;
import br.com.topaz.encurtador.exception.ValidacaoException;

import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UrlCurtaService {

    @Inject
    private UrlCurtaRepository repository;

    @Inject
    private GeradorCodigoCurto gerador;

    /**
     * Cria uma URL encurtada a partir da URL original e de um alias opcional.
     *
     * <p>O método é sincronizado para garantir que apenas uma solicitação
     * de criação seja processada por vez, conforme regra do motor de geração.
     * A sincronização fica na camada de serviço por se tratar de uma regra
     * de negócio/processamento, e não de responsabilidade do Resource ou
     * do Repository.</p>
     *
     * @param urlOriginal URL original que será encurtada
     * @param alias alias personalizado opcional
     * @return URL curta criada
     */
    public synchronized UrlCurta criar(String urlOriginal, String alias) {
        
        validarUrl(urlOriginal);

        String codigo;

        if (alias != null && !alias.trim().isEmpty()) {

            codigo = validarENormalizarAlias(alias);

            if (repository.existsByCodido(codigo)) {
                throw new AliasJaExisteException(codigo);
            }

        } else {

            do {
                codigo = gerador.gerar();
            } while (repository.existsByCodido(codigo));
        }

        UrlCurta urlCurta =
                new UrlCurta(
                        codigo,
                        urlOriginal.trim()
                );

        repository.save(urlCurta);

        return urlCurta;
    }

    public Optional<UrlCurta> findByCodigo(String codigo) {
        return repository.findByCodido(codigo);
    }

    private void validarUrl(String url) {

        if (url == null || url.trim().isEmpty()) {
            throw new ValidacaoException(
                    "A URL e obrigatoria."
            );
        }

        try {

            URI uri = new URI(url.trim());

            String scheme = uri.getScheme();

            if (scheme == null ||
                    (!"http".equalsIgnoreCase(scheme)
                            && !"https".equalsIgnoreCase(scheme))) {

                throw new ValidacaoException(
                        "A URL deve utilizar HTTP ou HTTPS."
                );
            }

            if (uri.getHost() == null ||
                    uri.getHost().trim().isEmpty()) {

                throw new ValidacaoException(
                        "Informe uma URL valida."
                );
            }

        } catch (URISyntaxException e) {

            throw new ValidacaoException(
                    "Informe uma URL valida."
            );
        }
    }

    private String validarENormalizarAlias(String alias) {

        String aliasNormalizado = alias.trim();

        if (!aliasNormalizado.matches(
                "^[A-Za-z0-9_-]{3,30}$")) {

            throw new ValidacaoException(
                    "O alias deve possuir entre 3 e 30 caracteres " +
                            "e utilizar apenas letras, numeros, hifen ou underline."
            );
        }

        return aliasNormalizado;
    }
}