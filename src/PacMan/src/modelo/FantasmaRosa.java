package modelo;

//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public final class FantasmaRosa extends Fantasma{
    private transient BufferedImage fantasma, fugindo, olhos;

    public FantasmaRosa(PainelJogo painel){
        super(painel);
        getImagem();
    }

    public FantasmaRosa(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        getImagem();
    }

    @Override
    public void getImagem() {
        try {
            fantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaRosa.png").toString()));
            fugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            olhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }

}
