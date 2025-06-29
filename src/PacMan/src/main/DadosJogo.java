package main;

import java.io.Serializable;
import java.util.ArrayList;

public class DadosJogo implements Serializable{
    public String[] mapa;
    public int numeroLinhas;
    public int numeroColunas;
    public int pontuacao;
    public int vidasPacMan;
    public int xPacMan;
    public int yPacMan;
    public int xInicialPacMan;
    public int yInicialPacMan;
    public int mapaAtual;
    public String direcaoPacMan;
    public ArrayList<TipoFantasma> tiposFantasmas;
    public ArrayList<Integer> xFantasmas;
    public ArrayList<Integer> yFantasmas;
    public ArrayList<Integer> xInicialFantasmas;
    public ArrayList<Integer> yInicialFantasmas;
    public ArrayList<String> direcaoFantasmas;
}
