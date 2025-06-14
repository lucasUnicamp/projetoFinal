package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Modelo.PacMan;

public class PainelJogo extends JPanel implements Runnable {
    private final int tamanhoPadraoTile = 16;
    private final int escala = 2;

    private final int tamanhoTile = tamanhoPadraoTile * escala;
    private final int numeroColunas = 40;
    private final int numeroLinhas = 20;
    private final int larguraTela = tamanhoTile * numeroColunas;
    private final int alturaTela = tamanhoTile * numeroLinhas;

    int FPS = 60;

    PacMan pacman;

    LeitorTeclado leitor = new LeitorTeclado();

    Thread gameThread;

    public PainelJogo() {
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(leitor);
        setFocusable(true);
        pacman = new PacMan(this, leitor);
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
        long timer = 0;
        int numeroDesenhos = 0;

        while(gameThread != null) { // loop principal do jogo

            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimoTempo) / intervaloDesenho;
            timer += (tempoAtual - ultimoTempo);
            ultimoTempo = tempoAtual;

            if(delta >= 1) { // ações que vão acontecer a cada tick
                atualizar();
                repaint();
                delta --;
                numeroDesenhos++;
            }

            if(timer >= 1000000000) {
                System.out.printf("FPS: %d\n", numeroDesenhos);
                numeroDesenhos = 0;
                timer = 0;
            }
        }
    }

    public void atualizar() {
        pacman.atualizar();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D caneta = (Graphics2D) g;

        caneta.setColor(Color.WHITE);

        pacman.desenhar(caneta);

        caneta.dispose();
    }

    public int getTamanhoTile() {
        return tamanhoTile;
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

}
