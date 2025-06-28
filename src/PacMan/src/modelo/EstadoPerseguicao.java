package modelo;

public enum EstadoPerseguicao {
    PERSEGUINDO(true),
    DISPERSO(false);

    private boolean estaPerseguindo;

    private EstadoPerseguicao(boolean estaPerseguindo) {
        this.estaPerseguindo = estaPerseguindo;
    }

    public boolean getEstadoPerseguicao() {
        return this.estaPerseguindo;
    }
}
