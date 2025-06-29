package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.PainelJogo;

public final class FantasmaVerde extends Fantasma{
    private int xbusca, ybusca;
    private ArrayList<Coordenada> cantos;

    public FantasmaVerde(PainelJogo painel){
        super(painel);
        cantos = new ArrayList<>();
        getImagem();
    }

    public FantasmaVerde(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        getImagem();
    }

    private void superiorEsquerdo(){
        Elemento[][] mapa = getPainelJogo().elementos;
        int i, j;
        for(i = 0; i < getPainelJogo().getNumeroLinhas(); i++){
            for(j = 0; j < getPainelJogo().getNumeroColunas(); j++){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto superior esquerdo
                    return;
                }
            }
        }
    }

    private void superiorDireito(){
        Elemento[][] mapa = getPainelJogo().elementos;
        int i, j;
        for(i = 0; i < getPainelJogo().getNumeroLinhas(); i++){
            for(j = getPainelJogo().getNumeroColunas() - 1; j > 0 ; j--){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto superior direito
                    return;
                }
            }
        }
    }

    private void inferiorEsquerdo(){
        Elemento[][] mapa = getPainelJogo().elementos;
        int i, j;
        for(i = getPainelJogo().getNumeroLinhas() - 1; i > 0; i--){
            for(j = 0; j < getPainelJogo().getNumeroColunas(); j++){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto inferior esquerdo
                    return;
                }
            }
        }
    }

    private void inferiorDireito(){
        Elemento[][] mapa = getPainelJogo().elementos;
        int i, j;
        for(i = getPainelJogo().getNumeroLinhas() - 1; i > 0; i--){
            for(j = getPainelJogo().getNumeroColunas() - 1; j > 0; j--){
                if(!mapa[i][j].ehColidivel()){
                    cantos.add(new Coordenada(j, i)); //canto inferior direito
                    return;
                }
            }
        }
    }

    public void definirCantos(){
        inferiorDireito();
        inferiorEsquerdo();
        superiorDireito();
        superiorEsquerdo();
    }

    public void funcaoPerseguicao(){
        if (cantos.isEmpty()){
            definirCantos();
        }
        Random r = new Random();
        int prox = r.nextInt(cantos.size());
        if(prox == cantos.size())
            prox--;
        
        if(!getCaminhoatual().isEmpty() && getMetaCaminho() != 0){
            buscarPonto();
        }
        else{
            menorCaminho(cantos.get(prox).getX(), cantos.get(prox).getY());
            setMetaCaminho(getCaminhoatual().size());
            buscarPonto();
        }
    }

    public void funcaoFuga(){ 
        //implementamos a fuga da mesma maneira que implementamos a busca: o mesmo algoritmo foi implementado
        //a diferenca eh que na busca o fantasma vermelho busca o menor caminho ate o pacman (perseguicao)
        //na fuga, por sua vez, o fantasma esta buscando o maior caminho ate o pacman, que atualizado repetidamente se assemelha a uma fuga
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
            if(getMetaCaminho() == 0 || calculaDistancia(xm, ym, xf, yf) < 4)
                menorCaminho(xm, ym);
            buscarPonto();
        }
    }

    @Override
    public void executarfuncao(){
        if(getEstadoPerseguicao().getEstadoPerseguicao()){
            funcaoPerseguicao();
        }
        else{
            funcaoFuga();
        }
    }

    @Override
    public void getImagem() {
        try {
            imagemFantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVerde.png").toString()));
            imagemFugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            imagemOlhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }

}
