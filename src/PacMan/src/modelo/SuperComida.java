package modelo;

import java.awt.Color;
import java.awt.Graphics2D;

import main.PainelJogo;

public class SuperComida extends Comestivel{

    public SuperComida(PainelJogo painelJogo, int x, int y) {
        super(painelJogo, x, y);
    }

    @Override
    public void desenhar(Graphics2D g) {
        int escala = getPainelJogo().getEscala();
        g.setColor(Color.WHITE);
        g.fillOval(getX(), getY(), 8*escala, 8*escala);
    }

    @Override
    public void comer() {
        int linhaMatriz = getY()/getPainelJogo().getTamanhoTile();
        int colunaMatriz = getX()/getPainelJogo().getTamanhoTile();
        getPainelJogo().elementos[linhaMatriz][colunaMatriz] = new EspacoVazio();
        getPainelJogo().aumentaPontuacao(50);
        getPainelJogo().ativaPerseguicao();
        getPainelJogo().comestiveis.removeFirst();
        getPainelJogo().getSom().tocarEfeito(1);
    }

    @Override
    public char getRepresentacao() {
        return 'O';
    }

}
