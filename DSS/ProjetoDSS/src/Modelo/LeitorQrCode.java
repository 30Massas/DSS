package Modelo;

import java.util.Random;

public class LeitorQrCode {

    /**
     * Implementação do método que gera um código de QR aleatório
     * @return Código QR gerado
     */
    public static String geraCodigoQR() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return (random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString());
    }

}
