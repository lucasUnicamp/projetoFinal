package modelo;

import java.awt.Graphics2D;

import interfaces.Elemento;

public class EspacoVazio implements Elemento{
    public EspacoVazio() {

    }
    
    @Override
    public void desenhar(Graphics2D g) {

    }

    @Override
    public boolean ehColidivel() {
        return false;
    }

}
