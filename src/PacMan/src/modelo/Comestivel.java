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

    public boolean ehColidivel() {
        return colidivel;
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }
}
