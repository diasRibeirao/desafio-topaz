package br.com.topaz.encurtador.service;

import javax.enterprise.context.ApplicationScoped;
import java.security.SecureRandom;

@ApplicationScoped
public class GeradorCodigoCurto {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    private static final int DEFAULT_LENGTH = 7;

    private final SecureRandom random =
            new SecureRandom();

    public String gerar() {
        return gerar(DEFAULT_LENGTH);
    }

    public String gerar(int length) {

        if (length <= 0) {
            throw new IllegalArgumentException(
                    "O tamanho do codigo deve ser maior que zero."
            );
        }

        StringBuilder code =
                new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int index =
                    random.nextInt(
                            CHARACTERS.length()
                    );

            code.append(
                    CHARACTERS.charAt(index)
            );
        }

        return code.toString();
    }
}