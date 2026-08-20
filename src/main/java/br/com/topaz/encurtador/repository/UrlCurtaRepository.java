package br.com.topaz.encurtador.repository;

import br.com.topaz.encurtador.domain.UrlCurta;

import java.util.Optional;

public interface UrlCurtaRepository {

    void save(UrlCurta urlCurta);

    Optional<UrlCurta> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

}