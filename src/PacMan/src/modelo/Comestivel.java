package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import main.PainelJogo;
import interfaces.Elemento;

public class Comestivel implements Elemento{
    private int posicaoX;
    private int posicaoY;
    private PainelJogo painelJogo;
    public boolean colidivel = false;

    public Comestivel(PainelJogo painelJogo, int x, int y) {
        setX(x);
        setY(y);
        this.painelJogo = painelJogo;
    }

    @Override public void desenhar(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(posicaoX, posicaoY, 10, 10);
    }

    public void comer() {
        int linhaMatriz = getY()/painelJogo.getTamanhoTile();
        int colunaMatriz = getX()/painelJogo.getTamanhoTile();
        painelJogo.elementos[linhaMatriz][colunaMatriz] = new EspacoVazio();
        painelJogo.aumentaPontuacao(1);
    }

    public boolean ehColidivel() {
        return colidivel;
    }
    
    public int getX() {
        return posicaoX;
    }

    public int getY() {
        return posicaoY;
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }
}
