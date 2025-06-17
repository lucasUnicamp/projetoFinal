package modelo;

import java.awt.Graphics2D;
import java.awt.Color;

public class Comida {
    private int posicaoX;
    private int posicaoY;

    public Comida(int x, int y) {
        setX(x);
        setY(y);
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
