package br.com.topaz.encurtador.dto;

public class CriarUrlCurtaResponse {

    private String urlCurta;
    private String codigo;
    private String urlOriginal;

    public CriarUrlCurtaResponse() {
    }

    public String getUrlCurta() {
        return urlCurta;
    }

    public void setUrlCurta(String urlCurta) {
        this.urlCurta = urlCurta;
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
}