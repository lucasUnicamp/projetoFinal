package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public final class FantasmaVermelho extends Fantasma{
    private transient BufferedImage fantasma, fugindo, olhos;

    public FantasmaVermelho(PainelJogo painel){
        super(painel);
        getImagem();
    }

    public FantasmaVermelho(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        getImagem();
    }

    @Override
    public void getImagem() {
        try {
            fantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVermelho.png").toString()));
            fugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            olhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }
    @Override
    public void executarfuncao(){
        //coordenadas do pac man
        int x = getPainelJogo().getPacMan().getX();
        int y = getPainelJogo().getPacMan().getY();
        //coordenadas do fantasma na matriz
        int xf = getX()/getPainelJogo().getTamanhoTile();
        int yf = getY()/getPainelJogo().getTamanhoTile();
        //coordenadas do destino na matriz
        int xm = x/getPainelJogo().getTamanhoTile();
        int ym = y/getPainelJogo().getTamanhoTile();


        //perseguir(x, y): caso o fantasma e seu destino nao estejam no mesmo ponto da matriz
        if(xf != xm || yf != ym){
            if(getCorrecoesPendentes() > 0){
                if(getDirecao().equals("direita"))
                    setX(getX() + getVelocidade());
                else  if(getDirecao().equals("esquerda"))
                    setX(getX() - getVelocidade());
                else if(getDirecao().equals("cima"))
                    setY(getY() - getVelocidade());
                else if(getDirecao().equals("baixo"))
                    setY(getY() + getVelocidade());
                setCorrecoesPendentes(getCorrecoesPendentes() - getVelocidade());
                if(getCorrecoesPendentes() <= 0){
                    centralizarX();
                    centralizarY();
                }
                return;
            }
            if(getMetaCaminho() == 0)
                menorCaminho(xm, ym);
            buscarPonto();
        }  
    }

    @Override
    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = fantasma;
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    }

}
