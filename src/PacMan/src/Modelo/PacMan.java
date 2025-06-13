package Modelo;

public class PacMan {
    private int posicaoX;
    private int posicaoY;
    private int velocidade;

    public PacMan() {
        setX(0);
        setY(0);
        velocidade = 10;
    }

    public void desenhar() {
        
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
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
