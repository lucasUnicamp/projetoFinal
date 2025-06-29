package modelo;

public enum EstadoPerseguicao {
    PERSEGUINDO(true, true),
    DISPERSO(false, false),
    MORTO(false, true);

    private boolean estaPerseguindo;
    private boolean heuristicaNegativa;

    private EstadoPerseguicao(boolean estaPerseguindo, boolean heuristicaNegativa) {
        this.estaPerseguindo = estaPerseguindo;
        this.heuristicaNegativa = heuristicaNegativa;
    }

    public boolean getEstadoPerseguicao() {
        return this.estaPerseguindo;
    }

    public boolean ehHeuristicaNegativa() {
        return this.heuristicaNegativa;
    }
}
