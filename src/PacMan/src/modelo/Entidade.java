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

    public void mover() {
        switch (getDirecao()) { // pac-man move continuamente para a direção que está apontando
            case "cima":
                if ((getY() - getPainelJogo().getTamanhoTile()/2) > getVelocidade())
                    setY(getY() - getVelocidade());
                else
                    setY(getPainelJogo().getTamanhoTile()/2);
                break;

            case "baixo":
                if ((getY() + getPainelJogo().getTamanhoTile()/2) < (getPainelJogo().getAltura() - getVelocidade()))
                    setY(getY() + getVelocidade());
                else 
                    setY(getPainelJogo().getAltura() - getPainelJogo().getTamanhoTile() / 2);
                break;

            case "esquerda":
                if ((getX() - getPainelJogo().getTamanhoTile()/2) > getVelocidade())
                    setX(getX() - getVelocidade());
                else
                    setX(getPainelJogo().getTamanhoTile()/2);
                break;

            case "direita":
                if ((getX() + getPainelJogo().getTamanhoTile()/2) < (getPainelJogo().getLargura() - getVelocidade()))
                    setX(getX() + getVelocidade());
                else
                    setX(getPainelJogo().getLargura() - getPainelJogo().getTamanhoTile()/2);
                break;
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
