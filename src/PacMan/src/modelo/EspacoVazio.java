package modelo;

import java.awt.Graphics2D;
import java.io.Serializable;

import interfaces.Elemento;

public class EspacoVazio implements Elemento, Serializable{
    public EspacoVazio() {

    }
    
    @Override
    public void desenhar(Graphics2D g) {

    }

    @Override
    public boolean ehColidivel() {
        return false;
    }

    public char getRepresentacao() {
        return ' ';
    }

}
