package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import main.PainelJogo;

public final class FantasmaRosa extends Fantasma{
    private transient BufferedImage fantasma, fugindo, olhos;
    private int xbusca, ybusca;
    private int distancia;

    public FantasmaRosa(PainelJogo painel){
        super(painel);
        if(getPainelJogo().getNumeroLinhas() > getPainelJogo().getNumeroColunas()){
            distancia = getPainelJogo().getNumeroLinhas() / 4;
        }
        else{
            distancia = getPainelJogo().getNumeroColunas() / 4;
        }
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
    
    private void definirDestino(){
        int xmin, ymin, xmax, ymax;
        Elemento[][] mapa = getPainelJogo().elementos;
        int xpac = getPainelJogo().getPacMan().getX()/getPainelJogo().getTamanhoTile();
        int ypac = getPainelJogo().getPacMan().getY()/getPainelJogo().getTamanhoTile();
        //busca da area de iteracao: otimizacao para nao percorrer a matriz
        if(xpac - distancia < 0)
            xmin = 0;
        else
            xmin = xpac - distancia;
        
        if(ypac - distancia < 0)
            ymin = 0;
        else    
            ymin = ypac - distancia;
    
        if(xpac + distancia > getPainelJogo().getNumeroColunas())
            xmax = getPainelJogo().getNumeroColunas();
        else
            xmax = xpac + distancia;
        
        if(ypac + distancia > getPainelJogo().getNumeroLinhas())
            ymax = getPainelJogo().getNumeroLinhas();
        else
            ymax = ypac + distancia;
        
        //if(getPainelJogo().get)
        for(int i = xmin; i < xmax; i++){
            for(int j = ymin; j < ymax; j++){
                if(!mapa[j][i].ehColidivel()){
                    if(distancia * distancia >= (j - xpac) * (j - xpac) + (i - ypac) * (i- ypac)){
                        xbusca = j;
                        ybusca = i;
                    }
                }
            }
        }
        xbusca = xpac;
        ybusca = ypac;
        
    }

     @Override
    public void executarfuncao(){
        //coordenadas do fantasma na matriz
        int xf = getX()/getPainelJogo().getTamanhoTile();
        int yf = getY()/getPainelJogo().getTamanhoTile();
        //coordenadas do destino na matriz
        definirDestino();


        //perseguir(x, y): caso o fantasma e seu destino nao estejam no mesmo ponto da matriz
        if(xf != xbusca || yf != ybusca){
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
                    setSpawn(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile());
                }
                return;
            }
            if(getMetaCaminho() == 0)
                menorCaminho(xbusca, ybusca);
            buscarPonto();
        } 
    }
    @Override
    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = fantasma;
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    }

}
