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

        if (codigo == KeyEvent.VK_W) {
            cimaPressionado = true;
        }

        if (codigo == KeyEvent.VK_A) {
            esquerdaPressionado = true;
        }

        if (codigo == KeyEvent.VK_S) {
            baixoPressionado = true;
        }

        if (codigo == KeyEvent.VK_D) {
            direitaPressionado = true;
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
