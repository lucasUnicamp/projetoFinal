package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LeitorTeclado implements KeyListener{

    public boolean cimaPressionado, baixoPressionado, direitaPressionado, esquerdaPressionado;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        System.out.printf("%d\n", codigo);

        if (!cimaPressionado && codigo == KeyEvent.VK_W) {
            cimaPressionado = true;
            esquerdaPressionado = false;
            direitaPressionado = false;
            baixoPressionado = false;
        }

        if (!esquerdaPressionado && codigo == KeyEvent.VK_A) {
            cimaPressionado = false;
            esquerdaPressionado = true;
            direitaPressionado = false;
            baixoPressionado = false;
        }

        if (!baixoPressionado && codigo == KeyEvent.VK_S) {
            cimaPressionado = false;
            esquerdaPressionado = false;
            direitaPressionado = false;
            baixoPressionado = true;
        }

        if (!direitaPressionado && codigo == KeyEvent.VK_D) {
            cimaPressionado = false;
            esquerdaPressionado = false;
            direitaPressionado = true;
            baixoPressionado = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int codigo = e.getKeyCode();

        if (codigo == KeyEvent.VK_W) {
            cimaPressionado = false;
        }

        if (codigo == KeyEvent.VK_A) {
            esquerdaPressionado = false;
        }

        if (codigo == KeyEvent.VK_S) {
            baixoPressionado = false;
        }

        if (codigo == KeyEvent.VK_D) {
            direitaPressionado = false;
        }
    }

}
