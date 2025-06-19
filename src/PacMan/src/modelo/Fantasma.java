package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public class Fantasma extends Entidade{

    private int estadoPerseguicao; //1 se o fantasma estiver perseguindo o pacman e 0 caso esteja no modo dispersando
    private int estadoexecucao; //1 se a sua execucao ja esta em andamento(perseguicao ou dispersao) e 0 caso contrario
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

    public int calculaDistancia(int x1, int y1, int x2, int y2){
        int dist;
        if(x2 > x1){
            if(y2 > y1)
                dist = (x2 - x1) + (y2 - y1);
            else
                dist = (x2 - x1) + (y1 - y2);
        }
        else{
            if(y2 > y1)
                dist = (x1 - x2) + (y2 - y1);
            else
                dist = (x1 - x2) + (y1 - y2);
        }
        return dist;
    }

    public void dispersar(int xf, int yf){

    }

    public void perseguir(){

    }

    public void executarfuncao(){

    }


}
