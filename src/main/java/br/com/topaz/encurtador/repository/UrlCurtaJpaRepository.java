package br.com.topaz.encurtador.repository;

import br.com.topaz.encurtador.domain.UrlCurta;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UrlCurtaJpaRepository implements UrlCurtaRepository {

    @PersistenceContext(unitName = "urlEncurtadorPU")
    private EntityManager entityManager;

    public void save(UrlCurta urlCurta) {
        entityManager.persist(urlCurta);
    }

    public Optional<UrlCurta> findByCodigo(String codigo) {
        List<UrlCurta> resultado =
                entityManager
                        .createQuery(
                                "SELECT u " +
                                        "FROM UrlCurta u " +
                                        "WHERE u.codigo = :codigo",
                                UrlCurta.class
                        )
                        .setParameter("codigo", codigo)
                        .setMaxResults(1)
                        .getResultList();

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(resultado.get(0));
    }

    public boolean existsByCodigo(String codigo) {
        Long quantidade =
                entityManager
                        .createQuery(
                                "SELECT COUNT(u) " +
                                        "FROM UrlCurta u " +
                                        "WHERE u.codigo = :codigo",
                                Long.class
                        )
                        .setParameter("codigo", codigo)
                        .getSingleResult();

        return quantidade > 0;
    }
}