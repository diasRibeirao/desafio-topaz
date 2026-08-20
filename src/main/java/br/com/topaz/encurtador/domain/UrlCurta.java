package br.com.topaz.encurtador.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "url_curta",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_url_curta_codigo",
                        columnNames = "codigo"
                )
        }
)
public class UrlCurta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "codigo",
            nullable = false,
            length = 30
    )
    private String codigo;

    @Column(
            name = "url_original",
            nullable = false,
            length = 2048
    )
    private String urlOriginal;

    @Column(
            name = "data",
            nullable = false
    )
    private LocalDateTime data;

    public UrlCurta() {
    }

    public UrlCurta(String codigo, String urlOriginal) {
        this.codigo = codigo;
        this.urlOriginal = urlOriginal;
        this.data = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
