package modelo;

import java.awt.Color;
import java.awt.Graphics2D;

import main.PainelJogo;

public class Comestivel {
    private int posicaoX;
    private int posicaoY;
    private PainelJogo painelJogo;

    public Comestivel(int x, int y, PainelJogo painelJogo) {
        setX(x);
        setY(y);
        this.painelJogo = painelJogo;
    }

    public void desenhar(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(posicaoX, posicaoX, 10, 10);
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }
}
