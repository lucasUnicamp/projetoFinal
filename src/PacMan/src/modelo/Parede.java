package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public class Parede implements Elemento {
    private int xMatriz;
    private int yMatriz;
    private int xReal;
    private int yReal;
    private BufferedImage paredeLado, paredeCheia, paredeBorda;
    private PainelJogo painelJogo;
    public boolean colidivel = true;

    public Parede(PainelJogo painelJogo, int x, int y) {
        this.painelJogo = painelJogo;
        setXMatriz(x);
        setYMatriz(y);
        xReal = x * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;
        yReal = y * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;

        getImagem();
    }

    // Instruções para desenhar as paredes na tela
    @Override
    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = null;

        switch (decideImagem()) {
            case 0:
                imagem = paredeCheia;
                break;
            case 1:
                imagem = paredeLado;
                break;
            case 2:
                imagem = paredeBorda;
                break;
        }
        caneta.drawImage(imagem, getXReal()  - (painelJogo.getTamanhoTile())/2, getYReal() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    /**
     * Importa o arquivo da pasta e associa-o ao objeto instanciado
     */
    public void getImagem() {
        try {
            paredeCheia = ImageIO.read(new File(Paths.get("resources", "imagens", "paredeCheia.png").toString()));
            paredeLado = ImageIO.read(new File(Paths.get("resources", "imagens", "paredeLado.png").toString())); 
            paredeBorda = ImageIO.read(new File(Paths.get("resources", "imagens", "paredeBorda.png").toString()));
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
            // Faz com que as paredes da borda sejam como "cheias" e deve evitar erro de index
            if (getXMatriz() - 1 < 0 || getXMatriz() + 1 >= painelJogo.getNumeroColunas() || 
                getYMatriz() - 1 < 0 || getYMatriz() + 1 >= painelJogo.getNumeroLinhas())
                return 2;

            // Separa as paredes cercadas por outras paredes para que recebam outro sprite
            if (mapa[getYMatriz() + 1].charAt(getXMatriz()) == 'p' && mapa[getYMatriz() - 1].charAt(getXMatriz()) == 'p' &&
                mapa[getYMatriz()].charAt(getXMatriz() + 1) == 'p' && mapa[getYMatriz()].charAt(getXMatriz() - 1) == 'p')
                return 0;

            return 1;
        } catch (IndexOutOfBoundsException erro) {
            System.err.println("!!! TENTATIVA DE DESENHAR PAREDE FORA DA MATRIZ !!!");      // Só para caso algo dê errado
            return 1;
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

    public int getXMatriz() {
        return xMatriz;
    }

    public int getYMatriz() {
        return yMatriz;
    }

    public int getXReal() {
        return xReal;
    }

    public int getYReal() {
        return yReal;
    }
}
