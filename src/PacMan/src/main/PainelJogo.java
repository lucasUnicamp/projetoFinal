package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import modelo.Comestivel;
import modelo.PacMan;
import modelo.Parede;

public class PainelJogo extends JPanel implements Runnable {
    private final int tamanhoPadraoTile = 16; // tamanho em pixels do tile original
    private final int escala = 2; // taxa de escalonamento do sprite na tela

    private final int tamanhoTile = tamanhoPadraoTile * escala; // tamanho em pixel de cada lado do tile
    private final int numeroColunas = 20; // numero de linhas de tiles
    private final int numeroLinhas = 20; // numero de colunas de tiles
    private final int larguraTela = tamanhoTile * numeroColunas; // largura em pixels do painel
    private final int alturaTela = tamanhoTile * numeroLinhas; // altura em pixels do painel
    
    private final ArrayList<Parede> paredes;
    private final ArrayList<Comestivel> comestiveis;
    private String[] mapa = {
        "pppppppppppppppppppp",
        "pccccccccccccccccccp",
        "pcppppppppcpppppppcp",
        "pcppppppppcpppppppcp",
        "pccccccccccpppppppcp",
        "pcppppppcpcpppppppcp",
        "pcppppppcpcpppppppcp",
        "pcppppcccccccccpppcp",
        "pcppppcppppppccpppcp",
        "pcppppccpppppccccccp",
        "pcppppccpppppccpppcp",
        "pcppppcccccccccpppcp",
        "pcppppppppcpppppppcp",
        "pcppppppppcpppppppcp",
        "pcpppcccccccccccppcp",
        "pcpppcpppppppppcppcp",
        "pccccccccccccccccccp",
        "pcpppcpppppppppcppcp",
        "pccccccccccccccccccp",
        "pppppppppppppppppppp",     
    };

    int FPS = 60;
    PacMan pacman;

    LeitorTeclado leitor = new LeitorTeclado(); // listener do teclado

    Thread gameThread;

    public PainelJogo() {
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(leitor);
        setFocusable(true);
        pacman = new PacMan(this, leitor);
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
        long timer = 0;

        while (gameThread != null) { // loop principal do jogo

            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimoTempo) / intervaloDesenho;
            ultimoTempo = tempoAtual;

            if (delta >= 1) { // ações que vão acontecer a cada tick
                atualizar();
                repaint();
                delta --;
            }
        }
    }

    public final void carregarElementos() {
        int i, j;
        for (i = 0; i < numeroLinhas; i++) {
            for (j = 0; j < numeroColunas; j++) {
                if (mapa[i].charAt(j) == 'p')   //parede
                    paredes.add(new Parede(this, j, i));
                else if (mapa[i].charAt(j) == 'c')      //comestiveis
                    comestiveis.add(new Comestivel(this, j * tamanhoTile + tamanhoTile / 4, i * tamanhoTile + tamanhoTile / 4));
            }
        }
    }
    
    // atualiza estado de todos os objetos
    public void atualizar() {
        pacman.atualizar();
    }

    // desenha tudo na tela
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D caneta = (Graphics2D) g;
        
        for(Parede p: this.paredes) {
            p.desenhar(caneta);
        }
        for(Comestivel c: this.comestiveis) {
            c.desenhar(caneta);
        }
   
        pacman.desenhar(caneta);

        caneta.dispose();
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

}
