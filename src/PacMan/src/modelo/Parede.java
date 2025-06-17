package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import main.PainelJogo;

public class Parede {
    private int posicaoX;
    private int posicaoY;
    private BufferedImage parede;

    private PainelJogo painelJogo;

    public Parede(PainelJogo painelJogo, int x, int y) {
        setX(x);
        setY(y);
        
        this.painelJogo = painelJogo;
    }

    // Instruções para desenhar as paredes na tela
    public void desenhar(Graphics2D caneta) {
        getImagem();
        String[] mapa = painelJogo.getMapa();
        caneta.drawImage(parede, getX()  - (painelJogo.getTamanhoTile())/2, getY() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    // Checar nas laterais de cada parede para ver qual tipo de parede deve desenhar

    // Importa os sprites nos arquivos
    public void getImagem() {
        try {
            parede = ImageIO.read(new File(Paths.get("resources", "Pac_Man_sprite_esquerda.png").toString())); 
        } catch (IOException e) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DA PAREDE !!!");
        }
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }

    public int getX() {
        return posicaoX;
    }

    public int getY() {
        return posicaoY;
    }
}
