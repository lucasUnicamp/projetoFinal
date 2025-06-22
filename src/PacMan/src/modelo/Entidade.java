package modelo;

import interfaces.Elemento;
import main.PainelJogo;

public class Entidade {
    private int posicaoX;
    private int posicaoY;
    private int velocidade; // pixels por frame
    private String direcao;
    private int alturaHitBox;
    private int larguraHitBox;

    private PainelJogo painelJogo;

    public Entidade(PainelJogo painelJogo, int x, int y, int velocidade, String direcao) {
        setX(x);   
        setY(y);
        this.velocidade = velocidade;
        this.direcao = direcao;
        this.alturaHitBox = painelJogo.getTamanhoTile()/2;
        this.larguraHitBox = painelJogo.getTamanhoTile()/2;
        
        this.painelJogo = painelJogo;
    }

    public Entidade(PainelJogo painelJogo) {
        this.painelJogo = painelJogo;
        this.alturaHitBox = painelJogo.getTamanhoTile()/2;
        this.larguraHitBox = painelJogo.getTamanhoTile()/2;

        setPadrao();
    }

    public void mover() {
        boolean colidiu = false;

        // cordenadas dos limites da hit box da entidade antes do movimento
        int yTopoHitBox = getY() - alturaHitBox/2;
        int yBaixoHitBox = getY() + alturaHitBox/2;
        int xDireitaHitBox = getX() + larguraHitBox/2;
        int xEsquerdaHitBox = getX() - larguraHitBox/2;

        // indices na matriz de elementos que cada borda da hit box está
        int linhaTopoHitBox = yTopoHitBox/painelJogo.getTamanhoTile(); 
        int linhaBaixoHitBox = yBaixoHitBox/painelJogo.getTamanhoTile();
        int colunaDireitaHitBox = xDireitaHitBox/painelJogo.getTamanhoTile();
        int colunaEsquerdaHitBox = xEsquerdaHitBox/painelJogo.getTamanhoTile();

        //linha e coluna na matriz da posição atual da entidade
        int linhaMatriz = getY()/painelJogo.getTamanhoTile();
        int colunaMatriz = getX()/painelJogo.getTamanhoTile();

        Elemento[][] elementos = painelJogo.elementos;

        switch (getDirecao()) { 
            case "cima":
                linhaTopoHitBox = (yTopoHitBox - getVelocidade())/painelJogo.getTamanhoTile(); // vê onde a hit box vai estar se o pac-man andar

                if ((getY() - getVelocidade()) <= getPainelJogo().getTamanhoTile()/2) {
                    colidiu = true; // colidiu com a borda do mapa
                    setY(getPainelJogo().getTamanhoTile()/2);
                    if(elementos[linhaMatriz][colunaMatriz] instanceof Tunel) {
                        setY(painelJogo.getAltura()-painelJogo.getTamanhoTile()/2);
                    }
                } else if (elementos[linhaTopoHitBox][colunaEsquerdaHitBox].ehColidivel() == true || elementos[linhaTopoHitBox][colunaDireitaHitBox].ehColidivel() == true) {
                    colidiu = true; // colidiu com parede
                }
                    
                break;

            case "baixo":
                linhaBaixoHitBox = (yBaixoHitBox + getVelocidade())/painelJogo.getTamanhoTile();

                if ((getY() + getVelocidade()) >= (getPainelJogo().getAltura() - getPainelJogo().getTamanhoTile()/2)) {
                    colidiu = true;
                    setY(getPainelJogo().getAltura() - getPainelJogo().getTamanhoTile() / 2);
                    if(elementos[linhaMatriz][colunaMatriz] instanceof Tunel) {
                        setY(painelJogo.getTamanhoTile()/2);
                    }
                } else if (elementos[linhaBaixoHitBox][colunaEsquerdaHitBox].ehColidivel() == true || elementos[linhaBaixoHitBox][colunaDireitaHitBox].ehColidivel() == true) {
                    colidiu = true;
                }
                    
                break;

            case "esquerda":
                colunaEsquerdaHitBox = (xEsquerdaHitBox - getVelocidade())/painelJogo.getTamanhoTile();

                if ((getX() - getVelocidade()) <= getPainelJogo().getTamanhoTile()/2) {
                    colidiu = true;
                    setX(getPainelJogo().getTamanhoTile()/2);
                    if(elementos[linhaMatriz][colunaMatriz] instanceof Tunel) {
                        setX(painelJogo.getLargura()-painelJogo.getTamanhoTile()/2);
                    }
                } else if (elementos[linhaBaixoHitBox][colunaEsquerdaHitBox].ehColidivel() == true || elementos[linhaTopoHitBox][colunaEsquerdaHitBox].ehColidivel() == true) {
                    colidiu = true;
                }
                break;

            case "direita":
                colunaDireitaHitBox = (xDireitaHitBox + getVelocidade())/painelJogo.getTamanhoTile();

                if ((getX() + getVelocidade()) >= (getPainelJogo().getLargura() - getPainelJogo().getTamanhoTile()/2)) {
                    colidiu = true;
                    setX(getPainelJogo().getLargura() - getPainelJogo().getTamanhoTile()/2);
                    if(elementos[linhaMatriz][colunaMatriz] instanceof Tunel) {
                        setX(painelJogo.getTamanhoTile()/2);
                    }
                } else if (elementos[linhaBaixoHitBox][colunaDireitaHitBox].ehColidivel() == true || elementos[linhaTopoHitBox][colunaDireitaHitBox].ehColidivel() == true){
                    colidiu = true;
                }
                break;
        }
        
        // caso o movimento não resulte em colisão, se movimenta
        if (!colidiu) { 
            switch (getDirecao()) {
                case "cima":
                    centralizarX(); // centraliza a entidade no caminho
                    setY(getY() - getVelocidade());
                    break;
                case "direita":
                    centralizarY();
                    setX(getX() + getVelocidade());
                    break;
                case "esquerda":
                    centralizarY();
                    setX(getX() - getVelocidade());
                    break;
                case "baixo":
                    centralizarX();
                    setY(getY() + getVelocidade());
                    break;
            }
        }
    }

    public void centralizarX() {
        setX((getX()/painelJogo.getTamanhoTile())*painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile()/2);
    }

    public void centralizarY() {
        setY((getY()/painelJogo.getTamanhoTile())*painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile()/2);
    }

    private void setPadrao() {
        int velocidade = 90; // pixels por segundo
        // Mantem uma posição padrão mesmo tendo o 'setSpawn' por precaução
        setX(getPainelJogo().getTamanhoTile() + getPainelJogo().getTamanhoTile()/2);   
        setY(getPainelJogo().getTamanhoTile() + getPainelJogo().getTamanhoTile()/2);
        setVelocidade((velocidade * getPainelJogo().getEscala()) / getPainelJogo().getFPS()); 
        setDirecao("direita");
    }

    /**
     * Posiciona a entidade no mapa na posição parametrizada. Método é chamado em 'PainelJogo', em que passa a posição
     * vinda do arquivo do mapa
     * @param x posição horizontal do spawn
     * @param y posição vertical do spawn
     */
    public void setSpawn(int x, int y) {
        setX(getPainelJogo().getTamanhoTile() * x + getPainelJogo().getTamanhoTile()/2);   
        setY(getPainelJogo().getTamanhoTile() * y + getPainelJogo().getTamanhoTile()/2);
    }

    public void setAlturaHitBox(int alturaHitBox) {
        this.alturaHitBox = alturaHitBox;
    }

    public void setLarguraHitBox(int larguraHitBox) {
        this.larguraHitBox = larguraHitBox;
    }

    public void setDimensoesHitBox(int dimensao) {
        setLarguraHitBox(dimensao);
        setAlturaHitBox(dimensao);
    }

    public void setVelocidade(int velocidade) {
        if(velocidade > 0) 
            this.velocidade = velocidade;
    }

    public void setDirecao(String direcao) {
        if(direcao == "direita" || direcao == "esquerda" || direcao == "cima" || direcao == "baixo") 
            this.direcao = direcao;
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }

    public String getDirecao() {
        return direcao;
    }

    public PainelJogo getPainelJogo() {
        return painelJogo;
    }    

    public int getVelocidade() {
        return velocidade;
    }

    public int getX() {
        return posicaoX;
    }

    public int getY() {
        return posicaoY;
    }
}
