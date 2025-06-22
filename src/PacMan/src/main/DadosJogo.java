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
    public String direcaoPacMan;
    public int xFantasma;
    public int yFantasma;
    public String direcaoFantasma;
    Fantasma fantasma;
}
