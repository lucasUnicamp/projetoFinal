package main;

import interfaces.Elemento;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import modelo.Comestivel;
import modelo.Fantasma;
import modelo.PacMan;
import modelo.Parede;

public class PainelJogo extends JPanel implements Runnable {
    private final int tamanhoPadraoTile = 16; // tamanho em pixels do tile original
    private final int escala = 2; // taxa de escalonamento do sprite na tela

    private final int tamanhoTile = tamanhoPadraoTile * escala; // tamanho em pixel de cada lado do tile
    private int numeroColunas; // numero de linhas de tiles
    private int numeroLinhas; // numero de colunas de tiles
    private int larguraTela; // largura em pixels do painel
    private int alturaTela; // altura em pixels do painel

    private int pontuacao;
    
    public final Elemento[][] elementos;
    private final ArrayList<Parede> paredes;
    private final ArrayList<Comestivel> comestiveis;
    private final TratadorMapa tratadorMapa;
    private String[] mapa;

    int FPS = 30;
    PacMan pacman;
    Fantasma fantasma;

    LeitorTeclado leitor = new LeitorTeclado(); // listener do teclado

    Thread gameThread;

    public PainelJogo(LeitorTeclado leitor) {
        this.leitor = leitor;
        tratadorMapa = new TratadorMapa(0);
        mapa = tratadorMapa.atribuirMapa();
        setNumeroColunas(tratadorMapa.getMapaLargura()); // numero de linhas de tiles
        setNumeroLinhas(tratadorMapa.getMapaAltura()); // numero de colunas de tiles
        larguraTela = tamanhoTile * numeroColunas; // largura em pixels do painel
        alturaTela = tamanhoTile * numeroLinhas; // altura em pixels do painel

        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        pacman = new PacMan(this, leitor);
        fantasma = new Fantasma(this);
        elementos = new Elemento[numeroLinhas][numeroColunas];
        comestiveis = new ArrayList<>();
        paredes = new ArrayList<>();
        this.carregarElementos();
    }

    public void comecarThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double intervaloDesenho = 1000000000 / FPS; // tempo em nano segundos entre cada atualização
        double delta = 0;
        long ultimoTempo = System.nanoTime();
        long tempoAtual;

        while (gameThread != null) { // loop principal do jogo

            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimoTempo) / intervaloDesenho;
            ultimoTempo = tempoAtual;

            if (delta >= 1) { // ações que vão acontecer a cada tick
                atualizar();
                repaint();
                delta --;
                //System.out.printf("pontos: %d\n", getPontuacao());
            }
        }
    }

    public final void carregarElementos() {
        int i, j;
        for (i = 0; i < getNumeroLinhas(); i++) {
            for (j = 0; j < getNumeroColunas(); j++) {
                if (mapa[i].charAt(j) == 'p') {  //parede
                    Parede parede = new Parede(this, j, i);
                    paredes.add(parede);
                    elementos[i][j] = parede;
                }
                else if (mapa[i].charAt(j) == 'c') {     //comestiveis
                    // - 5 pois é o raio do comestível
                    Comestivel comestivel = new Comestivel(this, j * tamanhoTile + tamanhoTile / 2 - 5, i * tamanhoTile + tamanhoTile / 2 - 5);
                    comestiveis.add(comestivel);
                    elementos[i][j] = comestivel;
                }
            }
        }
    }
    
    // atualiza estado de todos os objetos
    public void atualizar() {
        pacman.atualizar();
        fantasma.executarfuncao(pacman.getX(), pacman.getY());

    }

    // desenha tudo na tela
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D caneta = (Graphics2D) g;
        for(int i = 0; i < numeroLinhas; i++) {     //laco para o desenho dos elementos de parede e comestiveis
            for(int j = 0; j < numeroColunas; j++){
                elementos[i][j].desenhar(caneta);
            }
        }
        
        fantasma.desenhar(caneta);
        pacman.desenhar(caneta);

        caneta.dispose();
    }

    public void aumentaPontuacao(int aumento) {
        pontuacao += aumento;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setNumeroLinhas(int linhas) {
        numeroLinhas = linhas;
    }

    public void setNumeroColunas(int colunas) {
        numeroColunas = colunas;
    }

    public String[] getMapa() {
        return mapa;
    }

    public int getTamanhoTile() {
        return tamanhoTile;
    }

    public int getNumeroLinhas() {
        return numeroLinhas;
    }

    public int getNumeroColunas() {
        return numeroColunas;
    }

    public int getEscala() {
        return escala;
    }

    public int getAltura() {
        return alturaTela;
    }

    public int getLargura() {
        return larguraTela;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public int getFPS() {
        return FPS;
    }

}
