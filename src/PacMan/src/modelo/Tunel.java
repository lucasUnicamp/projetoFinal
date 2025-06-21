package modelo;

import interfaces.Elemento;
import main.PainelJogo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class Tunel implements Elemento {
    private int xMatriz;
    private int yMatriz;
    private int xReal;
    private int yReal;
    public boolean colidivel = false;
    private BufferedImage tunelVertical, tunelHorizontal;
    private PainelJogo painelJogo;

    public Tunel(PainelJogo painelJogo, int x, int y) {
        this.painelJogo = painelJogo;
        setXMatriz(x);
        setYMatriz(y);
        xReal = x * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;
        yReal = y * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;

        getImagem();
    }

    @Override public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = null;

        switch (0) {
            case 0:
                imagem = tunelVertical;
                break;
            case 1:
                imagem = tunelHorizontal;
                break;
        }
        caneta.drawImage(imagem, getXReal()  - (painelJogo.getTamanhoTile())/2, getYReal() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    public void getImagem() {
        try {
            tunelVertical = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelVertical.png").toString()));
            tunelHorizontal = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelHorizontal.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DA PAREDE !!!");
        }
    }

    public boolean ehColidivel() {
        return colidivel;
    }

    public void setXMatriz(int x) {
        xMatriz = x;
    }

    public void setYMatriz(int y) {
        yMatriz = y;
    }

    public int getXReal() {
        return xReal;
    }

    public int getYReal() {
        return yReal;
    }
}
