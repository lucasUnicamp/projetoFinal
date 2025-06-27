package main;

import interfaces.Elemento;
import menuPrincipal.PainelExterno;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import modelo.Comestivel;
import modelo.EspacoVazio;
import modelo.Fantasma;
import modelo.PacMan;
import modelo.Parede;
import modelo.Tunel;

public class PainelJogo extends JPanel implements Runnable {
    private final int tamanhoPadraoTile = 16; // tamanho em pixels do tile original
    private final int escala = 2; // taxa de escalonamento do sprite na tela

    private final int tamanhoTile = tamanhoPadraoTile * escala; // tamanho em pixel de cada lado do tile
    private int numeroColunas; // numero de linhas de tiles
    private int numeroLinhas; // numero de colunas de tiles
    private int larguraTela; // largura em pixels do painel
    private int alturaTela; // altura em pixels do painel
    private boolean recomecar;

    private int pontuacao;
    private boolean pausado;
    
    public Elemento[][] elementos;
    public ArrayList<Parede> paredes;
    public ArrayList<Comestivel> comestiveis;
    public ArrayList<Fantasma> fantasmas;
    private TratadorMapa tratadorMapa;
    private String[] mapa;

    private GameLoader gameLoader;

    int FPS = 30;
    PacMan pacman;

    LeitorTeclado leitor; // listener do teclado
    PainelExterno painelExterno;
    JPanel cards;
    private JComponent painelVidro;

    private Thread gameThread;

    public PainelJogo(LeitorTeclado leitor, PainelExterno painelExterno, JPanel cards, JComponent painelVidro) {
        this.cards = cards;
        this.leitor = leitor;
        this.painelExterno = painelExterno;
        this.painelVidro = painelVidro;

        this.gameLoader = new GameLoader(this);

        tratadorMapa = new TratadorMapa(0);

        novoJogo();

        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
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

        delayComeco();

        while (gameThread != null && !gameThread.isInterrupted()) { // loop principal do jogo

            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimoTempo) / intervaloDesenho;
            ultimoTempo = tempoAtual;

            if (delta >= 1) { // ações que vão acontecer a cada tick
                if(!estaPausado()) {
                    atualizar();
                    repaint();

                    if (recomecar) {
                        delayComeco();
                        setRecomecar(false);
                    }
                }
                delta --;
                try{
                    Thread.sleep(1000/FPS);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                //System.out.printf("pontos: %d\n", getPontuacao());
            }
        }
    }

    /**
     * Cria um delay de alguns segundos em que nada acontece para dar um tempo ao usuário
     * de processar que o jogo começou
     */
    public void delayComeco() {
        setPausado(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException erro) {
            System.err.println("!!! ERRO NA INTERRUPÇÃO DA THREAD !!!");
        }
        setPausado(false);
    }

    public void continuarJogo() {
        gameLoader.load();
    }

    public void salvarJogo() {
        gameLoader.salvar();
    }

    public void voltarMenu() {
        salvarJogo();
        painelVidro.setVisible(false);
        ((CardLayout) cards.getLayout()).show(cards, "painelMenu");
        getThread().interrupt();
    }

    public final void carregarElementos() {
        int i, j;
        for (i = 0; i < getNumeroLinhas(); i++) {
            for (j = 0; j < getNumeroColunas(); j++) {
                switch (mapa[i].charAt(j)) {
                    case '#':  //parede
                        Parede parede = new Parede(this, j, i);
                        paredes.add(parede);
                        elementos[i][j] = parede;
                        break;

                    case '.':     //comestiveis
                        // - 5 pois é o raio do comestível
                        Comestivel comestivel = new Comestivel(this, j * tamanhoTile + tamanhoTile / 2 - 5, i * tamanhoTile + tamanhoTile / 2 - 5);
                        comestiveis.add(comestivel);
                        elementos[i][j] = comestivel;
                        break;
                    
                    case '<':
                        Tunel tunel = new Tunel(this, j, i);
                        elementos[i][j] = tunel;
                        break;
                    
                    case ' ':
                        EspacoVazio espacoVazio = new EspacoVazio();
                        elementos[i][j] = espacoVazio;
                        break;

                    case 'P':
                        EspacoVazio spawnPacMan = new EspacoVazio();
                        elementos[i][j] = spawnPacMan;
                        pacman.setSpawn(j, i);
                        pacman.atualizarPosicaoInicial();
                        break;

                    case 'R':
                        EspacoVazio spawnFantVermelho = new EspacoVazio();
                        elementos[i][j] = spawnFantVermelho;
                        Fantasma fantasma = new Fantasma(this);
                        fantasma.setSpawn(j, i);
                        fantasma.atualizarPosicaoInicial();
                        fantasmas.add(fantasma);
                        break;
                }
            }
        }
    }
    
    // atualiza estado de todos os objetos
    public void atualizar() {
        for(Fantasma fantasma : fantasmas) {
            // Tem que ver se o fantasma não está comestível, se tiver o pacman não deve morrer 
            if(Math.abs(getPacMan().getX() - fantasma.getX()) <= getTamanhoTile() && Math.abs(getPacMan().getY() - fantasma.getY()) <= getTamanhoTile()) {
                // if (fantasma.getEstadoPerseguicao() == 0)
                pacman.morrer();
                resetPosicoes();
                setRecomecar(true);
                //else
            }
        }
        pacman.atualizar();
        for(Fantasma fantasma : fantasmas) {
            fantasma.executarfuncao(pacman.getX(), pacman.getY());
        }

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
        
        for(Fantasma fantasma : fantasmas)
            fantasma.desenhar(caneta);
        pacman.desenhar(caneta);

        if (pacman.getVidas() >= 1)
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 20*escala, getAltura() - 20*escala, getTamanhoTile(), getTamanhoTile(), null);
        if (pacman.getVidas() >= 2) 
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 40*escala, getAltura() - 20*escala, getTamanhoTile(), getTamanhoTile(), null);
        if (pacman.getVidas() >= 3)
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 60*escala, getAltura() - 20*escala, getTamanhoTile(), getTamanhoTile(), null);

        painelExterno.setTextoLabelPontos(String.format("Pontuação: %d", getPontuacao()));
        
        caneta.dispose();
    }

    
    public void novoJogo() {
        mapa = tratadorMapa.atribuirMapa();
        setNumeroColunas(tratadorMapa.getMapaLargura()); // numero de linhas de tiles
        setNumeroLinhas(tratadorMapa.getMapaAltura()); // numero de colunas de tiles
        larguraTela = tamanhoTile * numeroColunas; // largura em pixels do painel
        alturaTela = tamanhoTile * numeroLinhas; // altura em pixels do painel

        pacman = new PacMan(this, leitor);
        elementos = new Elemento[numeroLinhas][numeroColunas];
        fantasmas = new ArrayList<>();
        comestiveis = new ArrayList<>();
        paredes = new ArrayList<>();
        this.carregarElementos();
    }

    public void resetPosicoes() {
        pacman.irPosicaoInicial();
        for(int i = 0; i < fantasmas.size(); i++) {
            fantasmas.set(i, new Fantasma(this, fantasmas.get(i).getXInicial(), fantasmas.get(i).getYInicial(), fantasmas.get(i).getVelocidade(), fantasmas.get(i).getDirecao()));     
        }
    }

    public void aumentaPontuacao(int aumento) {
        pontuacao += aumento;
    }

    public boolean estaPausado() {
        return pausado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public void setRecomecar(boolean recomecar) {
        this.recomecar = recomecar;
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

    public void setTradadorMapa(TratadorMapa tratadorMapa) {
        this.tratadorMapa = tratadorMapa;
    }

    public void setMapa(String[] mapa) {
        this.mapa = mapa;
    }

    public void setPacMan(PacMan pacman) {
        this.pacman = pacman;
    }



    public Thread getThread() {
        return gameThread;
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

    public TratadorMapa getTratadorMapa() {
        return tratadorMapa;
    }

    public PacMan getPacMan() {
        return pacman;
    }

    public JComponent getPainelVidro() {
        return painelVidro;
    }

}
