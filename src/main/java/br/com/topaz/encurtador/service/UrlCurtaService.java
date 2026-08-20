package br.com.topaz.encurtador.service;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.repository.UrlCurtaRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UrlCurtaService {

    @Inject
    private UrlCurtaRepository repository;

    @Inject
    private GeradorCodigoCurto gerador;

    public UrlCurta create(String originalUrl, String alias) {

        String code;

        if (alias != null && !alias.trim().isEmpty()) {
            code = alias.trim();
        } else {
            do {
                code = gerador.gerar();
            } while (repository.existsByCodido(code));
        }

        UrlCurta urlCurta =
                new UrlCurta(code, originalUrl);

        repository.save(urlCurta);

        return urlCurta;
    }

    public Optional<UrlCurta> findByCode(String codido) {
        return repository.findByCodido(codido);
    }
}