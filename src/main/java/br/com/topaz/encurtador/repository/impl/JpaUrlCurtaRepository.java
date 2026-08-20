package br.com.topaz.encurtador.repository.impl;

import br.com.topaz.encurtador.domain.UrlCurta;
import br.com.topaz.encurtador.repository.UrlCurtaRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUrlCurtaRepository implements UrlCurtaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(UrlCurta urlCurta) {
        entityManager.persist(urlCurta);
    }

    @Override
    public Optional<UrlCurta> findByCodigo(String codigo) {

        TypedQuery<UrlCurta> query =
                entityManager.createQuery(
                        "SELECT s " +
                                "FROM UrlCurta s " +
                                "WHERE s.codigo = :codigo",
                        UrlCurta.class
                );

        query.setParameter(
                "codigo",
                codigo
        );

        query.setMaxResults(1);

        List<UrlCurta> resultados =
                query.getResultList();

        if (resultados.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                resultados.get(0)
        );
    }

    @Override
    public boolean existsByCodigo(String codigo) {

        Long quantidade =
                entityManager.createQuery(
                                "SELECT COUNT(s) " +
                                        "FROM UrlCurta s " +
                                        "WHERE s.codigo = :codigo",
                                Long.class
                        )
                        .setParameter(
                                "codigo",
                                codigo
                        )
                        .getSingleResult();

        return quantidade > 0;
    }


}