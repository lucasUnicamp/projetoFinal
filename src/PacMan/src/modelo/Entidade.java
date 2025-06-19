package modelo;

import interfaces.Elemento;
import main.LeitorTeclado;
import main.PainelJogo;

public class Entidade {
    private int posicaoX;
    private int posicaoY;
    private int velocidade;
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

        int xTopoHitBox = getX() + larguraHitBox; // cordenadas dos limites da hit box da entidade antes do movimento
        int xBaixoHitBox = getX() - larguraHitBox;
        int yDireitaHitBox = getY() + alturaHitBox;
        int yEsquerdaHitBox = getY() - alturaHitBox;

        int linhaTopoHitBox = xTopoHitBox/painelJogo.getTamanhoTile(); 
        int linhaBaixoHitBox = xBaixoHitBox/painelJogo.getTamanhoTile();
        int colunaDireitaHitBox = yDireitaHitBox/painelJogo.getTamanhoTile();
        int colunaEsquerdaHitBox = yEsquerdaHitBox/painelJogo.getTamanhoTile();

        Elemento[][] elementos = painelJogo.elementos;

        switch (getDirecao()) { // pac-man move continuamente para a direção que está apontando
            case "cima":
                linhaTopoHitBox = (xTopoHitBox + getVelocidade())/painelJogo.getTamanhoTile();

                if (elementos[linhaTopoHitBox][colunaEsquerdaHitBox].ehColidivel() == true || elementos[linhaTopoHitBox][colunaDireitaHitBox].ehColidivel() == true)
                    colidiu = true;
                else if ((getY() - getVelocidade()) <= getPainelJogo().getTamanhoTile()/2)
                    colidiu = true;
                    setY(getPainelJogo().getTamanhoTile()/2);
                    
                break;

            case "baixo":
                if ((getY() + getVelocidade()) < (getPainelJogo().getAltura() - getPainelJogo().getTamanhoTile()/2))
                    setY(getY() + getVelocidade());
                else 
                    setY(getPainelJogo().getAltura() - getPainelJogo().getTamanhoTile() / 2);
                break;

            case "esquerda":
                if ((getX() - getVelocidade()) > getPainelJogo().getTamanhoTile()/2)
                    setX(getX() - getVelocidade());
                else
                    setX(getPainelJogo().getTamanhoTile()/2);
                break;

            case "direita":
                if ((getX() + getVelocidade()) < (getPainelJogo().getLargura() - getPainelJogo().getTamanhoTile()/2))
                    setX(getX() + getVelocidade());
                else
                    setX(getPainelJogo().getLargura() - getPainelJogo().getTamanhoTile()/2);
                break;
        }
        
        if (!colidiu) {
            switch (getDirecao()) {
                case "cima":
                    setY(getY() + getVelocidade());
                    break;
                case "direita":
                    setX(getX() + getVelocidade());
                    break;
                case "esquerda":
                    setX(getX() - getVelocidade());
                    break;
                case "baixo":
                    setY(getY() - getVelocidade());
                    break;
            }
        }
    }

    private void setPadrao() {
        setX(painelJogo.getTamanhoTile());   
        setY(painelJogo.getTamanhoTile());
        setVelocidade(3);
        setDirecao("direita");
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
