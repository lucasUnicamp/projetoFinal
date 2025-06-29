package modelo;

import java.awt.Graphics2D;

import main.PainelJogo;

import java.awt.Color;

public class Comida extends Comestivel{

    public Comida(PainelJogo painelJogo, int x, int y) {
        super(painelJogo, x, y);
    }

    public void desenhar(Graphics2D g) {
        int escala = getPainelJogo().getEscala();
        g.setColor(Color.WHITE);
        g.fillOval(getX(), getY(), 5*escala, 5*escala);
    }

    @Override
    public char getRepresentacao() {
        return '.';
    }
}
