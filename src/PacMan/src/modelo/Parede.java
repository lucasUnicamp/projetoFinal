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
        getImagem();
    }

    // Instruções para desenhar as paredes na tela
    public void desenhar(Graphics2D caneta) {
        caneta.drawImage(parede, getX()  - (painelJogo.getTamanhoTile())/2, getY() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    /**
     * Importa o arquivo da pasta e associa-o ao objeto instanciado
     */
    public void getImagem() {
        try {
            switch (decideImagem()) {
                case 0:
                    parede = ImageIO.read(new File(Paths.get("resources", "paredeCheia.png").toString()));
                case 1:
                    parede = ImageIO.read(new File(Paths.get("resources", "paredeLado.png").toString()));
            }
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DA PAREDE !!!");
        }
    }

    /**
     * Baseado no que tem ao redor da posição em que se quer botar a parede, decide qual o tipo (parede ou cheio)
     * de sprite a ser posto
     * @return 0 se for uma parede cercada por outras paredes (cheio) ou se for uma posição da borda; 1 se for uma
     * parede não cercada por paredes 
     */
    public int decideImagem() {
        String[] mapa = painelJogo.getMapa();

        try {
            if (mapa[getX()].charAt(getY() + 1) == 'p' && mapa[getX() - 1].charAt(getY()) == 'p' 
            && mapa[getX() + 1].charAt(getY() + 1) == 'p' && mapa[getX()].charAt(getY() - 1) == 'p') 
                return 0;
            return 1;
        } catch (IndexOutOfBoundsException erro) {
            return 0;
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
