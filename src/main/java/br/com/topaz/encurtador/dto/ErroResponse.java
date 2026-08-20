package br.com.topaz.encurtador.dto;

public class ErroResponse {

    private int status;
    private String erro;
    private String mensagem;

    public ErroResponse() {
    }

    public ErroResponse(
            int status,
            String erro,
            String mensagem) {

        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }
}