package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.io.Serializable;
import main.PainelJogo;

public abstract class Comestivel implements Elemento, Serializable{
    private int posicaoX;
    private int posicaoY;
    private transient PainelJogo painelJogo;
    public boolean colidivel = false;

    public Comestivel(PainelJogo painelJogo, int x, int y) {
        setX(x);
        setY(y);
        this.painelJogo = painelJogo;
    }

    @Override public abstract void desenhar(Graphics2D g);

    public void comer() {
        int linhaMatriz = getY()/painelJogo.getTamanhoTile();
        int colunaMatriz = getX()/painelJogo.getTamanhoTile();
        painelJogo.elementos[linhaMatriz][colunaMatriz] = new EspacoVazio();
        painelJogo.aumentaPontuacao(10);
        painelJogo.comestiveis.removeFirst();
        //painelJogo.getSom().tocarEfeito(1);
    }

    public boolean ehColidivel() {
        return colidivel;
    }
    
    public abstract char getRepresentacao();

    public PainelJogo getPainelJogo() {
        return painelJogo;
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
