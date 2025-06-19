package modelo;

import main.LeitorTeclado;
import main.PainelJogo;

public class Entidade {
    private int posicaoX;
    private int posicaoY;
    private int velocidade;
    private String direcao;

    private PainelJogo painelJogo;

    public Entidade(PainelJogo painelJogo, int x, int y, int velocidade, String direcao) {
        setX(x);   
        setY(y);
        this.velocidade = velocidade;
        this.direcao = direcao;
        
        this.painelJogo = painelJogo;
    }

    public Entidade(PainelJogo painelJogo) {
        this.painelJogo = painelJogo;

        setPadrao();
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
