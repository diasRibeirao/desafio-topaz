package br.com.topaz.encurtador.dto;

public class CriarUrlCurtaRequest {

    private String url;
    private String alias;

    public CriarUrlCurtaRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}