package br.com.topaz.encurtador.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UrlCurta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String urlOriginal;
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
