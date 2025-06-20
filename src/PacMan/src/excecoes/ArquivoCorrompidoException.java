package excecoes;

public class ArquivoCorrompidoException extends Exception {
    public ArquivoCorrompidoException(String erro) {
        super(String.format("!!! ARQUIVO DE MAPA CORROMPIDO: %s !!!", erro));
    }
}
