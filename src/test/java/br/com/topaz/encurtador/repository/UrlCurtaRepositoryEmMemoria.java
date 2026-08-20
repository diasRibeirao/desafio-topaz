package br.com.topaz.encurtador.repository;

import br.com.topaz.encurtador.domain.UrlCurta;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UrlCurtaRepositoryEmMemoria
        implements UrlCurtaRepository {

    private final Map<String, UrlCurta> storage =
            new ConcurrentHashMap<String, UrlCurta>();

    @Override
    public void save(UrlCurta urlCurta) {

        storage.put(
                urlCurta.getCodigo(),
                urlCurta
        );
    }

    @Override
    public Optional<UrlCurta> findByCodigo(
            String codigo) {

        return Optional.ofNullable(
                storage.get(codigo)
        );
    }

    @Override
    public boolean existsByCodigo(
            String codigo) {

        return storage.containsKey(codigo);
    }
}