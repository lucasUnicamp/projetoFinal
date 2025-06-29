package main;

import interfaces.Elemento;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;

import menuPrincipal.PainelExterno;
import modelo.Comestivel;
import modelo.Comida;
import modelo.EspacoVazio;
import modelo.EstadoPerseguicao;
import modelo.Fantasma;
import modelo.FantasmaRosa;
import modelo.FantasmaVerde;
import modelo.FantasmaVermelho;
import modelo.PacMan;
import modelo.Parede;
import modelo.SuperComida;
import modelo.Tunel;

public class PainelJogo extends JPanel implements Runnable {
    private final int tamanhoPadraoTile = 16; // tamanho em pixels do tile original
    private int escala = 2; // taxa de escalonamento do sprite na tela

    private int tamanhoTile = tamanhoPadraoTile * escala; // tamanho em pixel de cada lado do tile
    private int numeroColunas; // numero de linhas de tiles
    private int numeroLinhas; // numero de colunas de tiles
    private int larguraTela; // largura em pixels do painel
    private int alturaTela; // altura em pixels do painel

    private int pontuacao;
    private int numeroMapaAtual = 0;
    private boolean pausado;
    private boolean vaiRecomecar = false, terminouTransicaoFase = true;
    private boolean gameOver = false, vaiGameOver = false;
    private boolean estaPerseguindo = false;
    
    public Elemento[][] elementos;
    public ArrayList<Parede> paredes;
    public ArrayList<Comestivel> comestiveis;
    public ArrayList<Fantasma> fantasmas;
    private TratadorMapa tratadorMapa;
    private String[] mapa;

    private GameLoader gameLoader;
    private Som som;

    int FPS = 30;
    PacMan pacman;

    LeitorTeclado leitor; // listener do teclado
    PainelExterno painelExterno;
    JPanel cards;
    private JComponent painelVidro;

    private Thread gameThread;

    public PainelJogo(LeitorTeclado leitor, PainelExterno painelExterno, JPanel cards, JComponent painelVidro, Som som) {
        this.cards = cards;
        this.leitor = leitor;
        this.painelExterno = painelExterno;
        this.painelVidro = painelVidro;

        this.som = som;
        this.gameLoader = new GameLoader(this);

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
        int framesPerseguicao = 0;

        delayComeco();
        
        while (gameThread != null && !gameThread.isInterrupted()) { // loop principal do jogo
            // Quando morre, muda o 'vaiRecomecar' para 'true', assim consegue colocar o delay do começo
            // como primeira ação. Cheque de 'gameOver' para não ter erro de interrupção na Thread
            if (vaiRecomecar && terminouTransicaoFase && !gameOver) {
                delayComeco();
                setVaiRecomecar(false);
            }
            
            tempoAtual = System.nanoTime();

            delta += (tempoAtual - ultimoTempo) / intervaloDesenho;
            ultimoTempo = tempoAtual;

            if (delta >= 1) { // ações que vão acontecer a cada tick
                if(!estaPausado()) {
                    atualizar();
                    repaint();
                    if (pacman.getVidas() <= 0) {
                        setPausado(true);
                        setGameOver(true);

                        mostrarTransicao("GAME OVER", () -> {
                            voltarMenuSemSalvar();
                            vaiGameOver = true;
                            mostrarTransicao("Saindo...", () -> {
                                setPausado(false);   
                            });
                        });
                    }


                    if (comestiveis.isEmpty()) {
                        setPausado(true);
                        int proximoMapa = tratadorMapa.getMapaEscolhido() + 1;

                        if (proximoMapa <= getTratadorMapa().getNumeroMapas()) {
                            setNumeroMapaAtual(getNumeroMapaAtual() + 1);
                            // Primeira transição para o fade in
                            mostrarTransicao("Fase Concluída!", () -> {
                                novoJogo(proximoMapa);
                                // Esse label que faz com que o mapa apareça na posição certa durante o load
                                painelExterno.setTextoLabelCanto(String.format("Preparando..."));
                                terminouTransicaoFase = false;
                                setVaiRecomecar(true);
                                // Segunda transição para o fade out
                                mostrarTransicao("Carregando próximo mapa...", () -> {
                                    setPausado(false);
                                    terminouTransicaoFase = true;
                                });
                            });
                        } else {
                            mostrarTransicao("Parabéns, você venceu!", () -> {
                                voltarMenuSemSalvar();
                            });
                        }
                    }
                }
                
                if (estaPerseguindo && framesPerseguicao >= 30*FPS) {
                    pararPerseguicao();
                    framesPerseguicao = 0;
                } else if (estaPerseguindo){
                    framesPerseguicao++;
                }
                
                delta--;
                try {
                    Thread.sleep(1000/FPS);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    //Transição de Fase
    public void mostrarTransicao(String mensagem, Runnable proximaAcao) {
        TransicaoFase transicao = new TransicaoFase(mensagem, () -> {
            painelVidro.setVisible(false); // esconde a transição
            proximaAcao.run();             // executa o que vier depois
        });
        
        painelVidro.removeAll();
        painelVidro.setLayout(new BorderLayout());
        painelVidro.add(transicao, BorderLayout.CENTER);
        painelVidro.setVisible(true);

        if (gameOver) {
            if (!vaiGameOver) {
                transicao.setOpacidade(0f);
                transicao.iniciar();
            }
            else {
                transicao.setOpacidade(1f);
                transicao.finalizar();
            }
        } else {
        // Jogo registra que deve recomeçar apenas após o fade in, então começa fazendo o fade in
        if (!vaiRecomecar) {
            transicao.setOpacidade(0f);
            transicao.iniciar();
        }
        else {
            transicao.setOpacidade(1f);
            transicao.finalizar();
        }
    }
}

    /**
     * Cria um delay de alguns segundos em que nada acontece para dar um tempo ao usuário
     * de processar que o jogo começou. Deve ser chamado no início do jogo e toda vez que
     * o pacman morrer
     */
    public void delayComeco() {
        setPausado(true);
        try {
            painelExterno.setTextoLabelCanto(String.format("3..."));
            Thread.sleep(30000/FPS);
            painelExterno.setTextoLabelCanto(String.format("2..."));
            Thread.sleep(30000/FPS);
            painelExterno.setTextoLabelCanto(String.format("1..."));
            Thread.sleep(30000/FPS);
            painelExterno.setTextoLabelCanto(String.format("Mapa %s", getNumeroMapaAtual()));
        } catch (InterruptedException erro) {
            System.err.println("!!! ERRO NA INTERRUPÇÃO DA THREAD DELAY COMECO !!!");
        }
        setPausado(false);
    }

    public void carregarJogo() {
        pacman = new PacMan(this, leitor);
        fantasmas = new ArrayList<>();
        comestiveis = new ArrayList<>();
        paredes = new ArrayList<>();
        gameLoader.load();

        Rectangle tamanhoTela = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        int escalaPossivel1 = (int) (tamanhoTela.width)/ (numeroColunas * tamanhoPadraoTile);
        int escalaPossivel2 = (int) (0.9*tamanhoTela.height) / (numeroLinhas * tamanhoPadraoTile);

        if(escalaPossivel1 > escalaPossivel2) {
            escala = escalaPossivel2;
        } else {
            escala = escalaPossivel1;
        }

        if(escala <= 0) {
            escala = 1;
        }

        tamanhoTile = tamanhoPadraoTile * escala;
        larguraTela = tamanhoTile * numeroColunas; // largura em pixels do painel
        alturaTela = tamanhoTile * numeroLinhas; // altura em pixels do painel

        setPreferredSize(new Dimension(larguraTela, alturaTela));
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

    public void voltarMenuSemSalvar() {
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
                        Comestivel comida = new Comida(this, j * tamanhoTile + tamanhoTile / 2 - 5, i * tamanhoTile + tamanhoTile / 2 - 5);
                        comestiveis.add(comida);
                        elementos[i][j] = comida;
                        break;
                    case 'O':
                        Comestivel supercomida = new SuperComida(this, j * tamanhoTile + tamanhoTile / 2 - 5, i * tamanhoTile + tamanhoTile / 2 - 5);
                        comestiveis.add(supercomida);
                        elementos[i][j] = supercomida;
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

                    case 'V':
                        EspacoVazio spawnFantVermelho = new EspacoVazio();
                        elementos[i][j] = spawnFantVermelho;
                        FantasmaVermelho fantVermelho = new FantasmaVermelho(this);
                        fantVermelho.setSpawn(j, i);
                        fantVermelho.atualizarPosicaoInicial();
                        fantasmas.add(fantVermelho);
                        break;

                    case 'R':
                        EspacoVazio spawnFantRosa = new EspacoVazio();
                        elementos[i][j] = spawnFantRosa;
                        FantasmaRosa fantRosa = new FantasmaRosa(this);
                        fantRosa.setSpawn(j, i);
                        fantRosa.atualizarPosicaoInicial();
                        fantasmas.add(fantRosa);
                        break;
                    case 'G':
                        EspacoVazio spawnFantVerde = new EspacoVazio();
                        elementos[i][j] = spawnFantVerde;
                        FantasmaVerde fantVerde = new FantasmaVerde(this);
                        fantVerde.setSpawn(j, i);
                        fantVerde.atualizarPosicaoInicial();
                        fantasmas.add(fantVerde);
                        break;
                }
            }
        }
    }
    
    // atualiza estado de todos os objetos
    public void atualizar() {
        for (Fantasma fantasma : fantasmas) {
            // Tem que ver se o fantasma não está comestível, se tiver o pacman não deve morrer 
            if (Math.abs(getPacMan().getX() - fantasma.getX()) <= getTamanhoTile() && Math.abs(getPacMan().getY() - fantasma.getY()) <= getTamanhoTile()) {
                if (fantasma.getEstadoPerseguicao().getEstadoPerseguicao()) {
                    pacmanMorreu();
                    // Se é a última vida do pacman, não quero que as posições sejam resetadas pois ficaria estranhao
                    if (pacman.getVidas() > 0) {
                        resetPosicoes();
                        setVaiRecomecar(true);
                    } else {
                        setPausado(true);
                    }
                } else if (fantasma.getEstadoPerseguicao() == EstadoPerseguicao.DISPERSO){
                    fantasma.perder();
                }
            }
        }
        pacman.atualizar();
        for (Fantasma fantasma : fantasmas) {
            fantasma.executarfuncao();
        }
    }

    /**
     * Método para criar um delay e atualizar o estado do pacman após levar dano 
     */
    public void pacmanMorreu() {
        setPausado(true);
        try {
            pacman.morrer();
            repaint();
            painelExterno.setTextoLabelCanto(String.format("Ouch!"));
            Thread.sleep(30000/FPS); 
            pacman.setEstaMorto(false);
        } catch (InterruptedException erro) {
            System.err.println("!!! ERRO NA INTERRUPÇÃO DA THREAD !!!");
        }
        setPausado(false);
    }

    public void ativaPerseguicao() {
        for(Fantasma fantasma : fantasmas) {
            if(fantasma.getEstadoPerseguicao() != EstadoPerseguicao.MORTO)
                fantasma.acionarFuga();
        }
        estaPerseguindo = true;
    }

    public void pararPerseguicao() {
        for(Fantasma fantasma : fantasmas) {
            if(fantasma.getEstadoPerseguicao() != EstadoPerseguicao.MORTO)
                fantasma.encerrarFuga();
        }
        estaPerseguindo = false;
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
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 10*escala, getAltura() - 10*escala, getTamanhoTile()/2, getTamanhoTile()/2, null);
        if (pacman.getVidas() >= 2) 
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 30*escala, getAltura() - 10*escala, getTamanhoTile()/2, getTamanhoTile()/2, null);
        if (pacman.getVidas() >= 3)
            caneta.drawImage(pacman.getImagemRepouso(), getLargura() - 50*escala, getAltura() - 10*escala, getTamanhoTile()/2, getTamanhoTile()/2, null);

        painelExterno.setTextoLabelPontos(String.format("Pontuação: %d", getPontuacao()));
        
        caneta.dispose();
    }

    public void novoJogo(int mapaAtual) {
        setTratadorMapa(new TratadorMapa(mapaAtual));
        mapa = tratadorMapa.atribuirMapa();
        setNumeroColunas(tratadorMapa.getMapaLargura()); // numero de linhas de tiles
        setNumeroLinhas(tratadorMapa.getMapaAltura()); // numero de colunas de tiles
        resetarValores();

        Rectangle tamanhoTela = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        int escalaPossivel1 = (int) (tamanhoTela.width)/ (numeroColunas * tamanhoPadraoTile);
        int escalaPossivel2 = (int) (0.9*tamanhoTela.height) / (numeroLinhas * tamanhoPadraoTile);

        if(escalaPossivel1 > escalaPossivel2) {
            escala = escalaPossivel2;
        } else {
            escala = escalaPossivel1;
        }

        if(escala <= 0) {
            escala = 1;
        }

        tamanhoTile = tamanhoPadraoTile * escala;
        larguraTela = tamanhoTile * numeroColunas; // largura em pixels do painel
        alturaTela = tamanhoTile * numeroLinhas; // altura em pixels do painel
        setPreferredSize(new Dimension(larguraTela, alturaTela));

        pacman = new PacMan(this, leitor);
        elementos = new Elemento[numeroLinhas][numeroColunas];
        fantasmas = new ArrayList<>();
        comestiveis = new ArrayList<>();
        paredes = new ArrayList<>();
        this.carregarElementos();
    }

    public void resetarValores() {
        vaiRecomecar = false;
        terminouTransicaoFase = true;
        gameOver = false;
        vaiGameOver = false;
        setPontuacao(0);
        setNumeroMapaAtual(0);
        setGameOver(false);
    }


    public void resetPosicoes() {
        pacman.irPosicaoInicial();
        for(Fantasma fantasma: fantasmas) {
            fantasma.setSpawn(fantasma.getXInicial()/getTamanhoTile(), fantasma.getYInicial()/getTamanhoTile());   
            fantasma.setMetaCaminho(0); 
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

    public void setVaiRecomecar(boolean vaiRecomecar) {
        this.vaiRecomecar = vaiRecomecar;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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

    public void setNumeroMapaAtual(int num) {
        numeroMapaAtual = num;
    }
    
    public void setTerminouTransicaoFase(boolean b) {
        terminouTransicaoFase = b;
    }

    public void setTratadorMapa(TratadorMapa tratadorMapa) {
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

    public int getNumeroMapaAtual() {
        return numeroMapaAtual;
    }

    public boolean getTerminouTransicaoFase() {
        return terminouTransicaoFase;
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

    public Som getSom() {
        return som;
    }
}
