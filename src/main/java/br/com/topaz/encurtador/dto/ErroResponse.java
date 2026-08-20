package br.com.topaz.encurtador.dto;

import java.time.LocalDateTime;

public class ErroResponse {

    private int status;
    private String erro;
    private String mensagem;
    private String caminho;
    private LocalDateTime timestamp;

    public ErroResponse() {
    }

    public ErroResponse(
            int status,
            String erro,
            String mensagem,
            String caminho) {

        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
        this.timestamp = LocalDateTime.now();
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

    public String getCaminho() {
        return caminho;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}