package modelo;

import interfaces.Elemento;
import main.PainelJogo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class Tunel implements Elemento, Serializable{
    private int xMatriz;
    private int yMatriz;
    private int xReal;
    private int yReal;
    public boolean colidivel = false;
    private transient BufferedImage tunelNorte, tunelSul, tunelLeste, tunelOeste;
    private transient PainelJogo painelJogo;

    public Tunel(PainelJogo painelJogo, int x, int y) {
        this.painelJogo = painelJogo;
        setXMatriz(x);
        setYMatriz(y);
        xReal = x * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;
        yReal = y * painelJogo.getTamanhoTile() + painelJogo.getTamanhoTile() / 2;

        getImagem();
    }

    // Instruções para desenhar os túneis na tela
    @Override public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = null;

        switch (decideImagem()) {
            case 0:
                imagem = tunelNorte;
                break;
            case 1:
                imagem = tunelSul;
                break;
            case 2:
                imagem = tunelLeste;
                break;
            case 3:
                imagem = tunelOeste;
                break;
        }
        caneta.drawImage(imagem, getXReal()  - (painelJogo.getTamanhoTile())/2, getYReal() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    /**
     * Importa o arquivo da pasta e associa-o ao objeto instanciado
     */
    public void getImagem() {
        try {
            tunelNorte = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelNorte.png").toString()));
            tunelSul = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelSul.png").toString()));
            tunelLeste = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelLeste.png").toString()));
            tunelOeste = ImageIO.read(new File(Paths.get("resources", "imagens", "tunelOeste.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO TÚNEL !!!");
        }
    }

    /**
     * Baseado na posição do túnel na matriz, decide qual rotação do sprite deve ser usada
     * @return 0 se o túnel estiver no lado norte, 1 se estiver no sul, 2 no leste e 3 no oeste 
     */
    public int decideImagem() {
        try {
            if (getYMatriz() - 1 < 0)       // Está no topo (norte)
                return 0;
            if (getYMatriz() + 1 >= painelJogo.getNumeroLinhas())       // Está embaixo (sul)
                return 1;
            if (getXMatriz() + 1 >= painelJogo.getNumeroColunas())      // Está no lado direito (leste)
                return 2;
            if (getXMatriz() - 1 < 0)       // Está no lado esquerdo (oeste)
                return 3;
            return 0;

        } catch (IndexOutOfBoundsException erro) {
            System.err.println("!!! TENTATIVA DE DESENHAR TÚNEL FORA DA MATRIZ !!!");      // Só para caso algo dê errado
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

    public char getRepresentacao() {
        return '<';
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
