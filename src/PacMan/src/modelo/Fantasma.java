package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public class Fantasma extends Entidade{

    private estadoPerseguicao;
    private BufferedImage provisoria;

    public Fantasma(PainelJogo painel) {
        super(painel);
        setX(getPainelJogo().getTamanhoTile() * 10 + getPainelJogo().getTamanhoTile()/2);   
        setY(getPainelJogo().getTamanhoTile() * 11 + getPainelJogo().getTamanhoTile()/2);

        estadoPerseguicao = 1;
        setVelocidade((100 * getPainelJogo().getEscala()) / getPainelJogo().getFPS()); 

        setDirecao("cima");

        try {
            provisoria = ImageIO.read(new File(Paths.get("resources", "fantasma.png").toString()));
        } catch (IOException e) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DO SPRITE DO FANTASMA !!!");
        }
    }

    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = provisoria;
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    }


}
