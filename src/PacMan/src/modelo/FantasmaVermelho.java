package modelo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import enums.EstadoPerseguicao;
import main.PainelJogo;

public final class FantasmaVermelho extends Fantasma{

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
            imagemFantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVermelho.png").toString()));
            imagemFugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            imagemOlhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }

    public void funcaoRetorno() {
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
                    setX(xf * getPainelJogo().getTamanhoTile() + getPainelJogo().getTamanhoTile()/2);
                    setY(yf * getPainelJogo().getTamanhoTile() + getPainelJogo().getTamanhoTile()/2);  
                }
                return;
            }
            buscarPonto();
        }  
        if(getMetaCaminho() == 0)
            setEstadoPerseguicao(EstadoPerseguicao.DISPERSO);
    }

    @Override
    public void executarfuncao(){
        if(getEstadoPerseguicao() == EstadoPerseguicao.MORTO) {
            funcaoMovimento(getXInicial()/getPainelJogo().getTamanhoTile(), getYInicial()/getPainelJogo().getTamanhoTile());
        } else {
            funcaoMovimento(getPainelJogo().getPacMan().getX()/getPainelJogo().getTamanhoTile(), getPainelJogo().getPacMan().getY()/getPainelJogo().getTamanhoTile());
        }
    }

}
