package enums;

public enum EstadoPerseguicao {
    PERSEGUINDO(true, true),
    DISPERSO(false, false),
    MORTO(false, true);

    private boolean estaPerseguindo;
    private boolean heuristicaPositiva;

    private EstadoPerseguicao(boolean estaPerseguindo, boolean heuristicaPositiva) {
        this.estaPerseguindo = estaPerseguindo;
        this.heuristicaPositiva = heuristicaPositiva;
    }

    public boolean getEstadoPerseguicao() {
        return this.estaPerseguindo;
    }

    public boolean ehHeuristicaPositiva() {
        return this.heuristicaPositiva;
    }
}
