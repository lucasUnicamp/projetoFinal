package modelo;

//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import interfaces.Elemento;
import main.PainelJogo;

public final class FantasmaVerde extends Fantasma{
    private transient BufferedImage fantasma, fugindo, olhos;
    private int xbusca, ybusca;
    private ArrayList<Coordenada> cantos;

    public FantasmaVerde(PainelJogo painel){
        super(painel);
        cantos = new ArrayList<>();
        definirCantos();
        getImagem();
    }

    public FantasmaVerde(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        getImagem();
    }

    public definirCantos(){
        Elemento[][] mapa = getPainelJogo().elementos;
        int i, j;
        for(i = 0; i < getPainelJogo().getNumeroLinhas(); i++){
            for(j = 0; j < getPainelJogo().getNumeroColunas(); j++){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto superior esquerdo
                    break;
                }
            }
        }
        for(i = 0; i < getPainelJogo().getNumeroLinhas(); i++){
            for(j = getPainelJogo().getNumeroColunas() - 1; j > 0 ; j--){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto superior direito
                    break;
                }
            }
        }
        for(i = getPainelJogo().getNumeroLinhas() - 1; i > 0; i--){
            for(j = 0; j < getPainelJogo().getNumeroColunas(); j++){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto inferior esquerdo
                    break;
                }
            }
        }
        for(i = getPainelJogo().getNumeroLinhas() - 1; i > 0; i--){
            for(j = 0; j < getPainelJogo().getNumeroColunas(); j++){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto inferior direito
                    break;
                }
            }
        }
    }

    @Override
    public void getImagem() {
        try {
            fantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVerde.png").toString()));
            fugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            olhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }

}
