package main;

import java.io.Serializable;
import java.util.ArrayList;

import interfaces.Elemento;
import modelo.Comestivel;
import modelo.Fantasma;
import modelo.PacMan;
import modelo.Parede;

public class DadosJogo implements Serializable{
    public String[] mapa;
    public int pontuacao;
    public int vidasPacMan;
    public int xPacMan;
    public int yPacMan;
    public int xInicialPacMan;
    public int yInicialPacMan;
    public int mapaAtual;
    public String direcaoPacMan;
    public ArrayList<Integer> xFantasmas;
    public ArrayList<Integer> yFantasmas;
    public ArrayList<Integer> xInicialFantasmas;
    public ArrayList<Integer> yInicialFantasmas;
    public ArrayList<String> direcaoFantasmas;
    Fantasma fantasma;
}
