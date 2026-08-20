package br.com.topaz.encurtador.repository;

import br.com.topaz.encurtador.domain.UrlCurta;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UrlCurtaRepository {

    private final Map<String, UrlCurta> storage =
            new ConcurrentHashMap<String, UrlCurta>();

    public void save(UrlCurta urlCurta) {
        storage.put(urlCurta.getCodigo(), urlCurta);
    }

    public Optional<UrlCurta> findByCodido(String codigo) {
        return Optional.ofNullable(storage.get(codigo));
    }

    public boolean existsByCodido(String codido) {
        return storage.containsKey(codido);
    }
}