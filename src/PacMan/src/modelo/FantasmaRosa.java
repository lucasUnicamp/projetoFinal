package modelo;

import interfaces.Elemento;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.PainelJogo;

public final class FantasmaRosa extends Fantasma{
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
            imagemFantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaRosa.png").toString()));
            imagemFugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            imagemOlhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }
    
    private void definirDestino(){
        ArrayList<Coordenada> possiveis = new ArrayList<>();
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
        
        for(int i = xmin; i < xmax; i++){
            for(int j = ymin; j < ymax; j++){
                if(!mapa[j][i].ehColidivel()){
                    if(distancia * distancia <= (i - xpac) * (i - xpac) + (j - ypac) * (j- ypac))
                        possiveis.add(new Coordenada(i, j)); 
                }
            }
        }
        if (!possiveis.isEmpty()){
            Random r = new Random();
            int pos = r.nextInt(possiveis.size());
            if(pos == possiveis.size())
                pos--;
            xbusca = possiveis.get(pos).getX();
            ybusca = possiveis.get(pos).getY();
        }
        else{
            xbusca = xpac;
            ybusca = ypac;
        }
        
    }
/* 
    public void funcaoPerseguicao(){
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
                else if(getDirecao().equals("esquerda"))
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
            if(getMetaCaminho() == 0)
                menorCaminho(xbusca, ybusca);
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
            if(getMetaCaminho() == 0)
                menorCaminho(xm, ym);
            buscarPonto();
        }
    }

    public void funcaoRetorno() { // função para retornar à base pelo menor caminho
        //coordenadas do fantasma na matriz
        int xf = getX()/getPainelJogo().getTamanhoTile();
        int yf = getY()/getPainelJogo().getTamanhoTile();
        //coordenadas do destino na matriz
        int xm = getXInicial()/getPainelJogo().getTamanhoTile();
        int ym = getYInicial()/getPainelJogo().getTamanhoTile();


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
            setEstadoPerseguicao(EstadoPerseguicao.PERSEGUINDO);
        System.out.println(getMetaCaminho());
    }
*/

    @Override
    public void executarfuncao(){
        if (getEstadoPerseguicao().getEstadoPerseguicao()){
            definirDestino();
            funcaoMovimento(xbusca, ybusca);
        } else if (getEstadoPerseguicao() == EstadoPerseguicao.DISPERSO){
            funcaoMovimento(getPainelJogo().getPacMan().getX()/getPainelJogo().getTamanhoTile(), getPainelJogo().getPacMan().getY()/getPainelJogo().getTamanhoTile());
        } else {
            funcaoMovimento(getXInicial()/getPainelJogo().getTamanhoTile(), getYInicial()/getPainelJogo().getTamanhoTile());
        }
    }

}
