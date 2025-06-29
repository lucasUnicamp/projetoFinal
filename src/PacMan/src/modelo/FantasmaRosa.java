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
        if((xpac - distancia - 1) < 0)
            xmin = 0;
        else
            xmin = (xpac - distancia - 1);
        
        if((ypac - distancia - 1) < 0)
            ymin = 0;
        else    
            ymin = ypac - distancia - 1;
    
        if((xpac + distancia + 1) > getPainelJogo().getNumeroColunas())
            xmax = getPainelJogo().getNumeroColunas();
        else
            xmax = xpac + distancia + 1;
        
        if((ypac + distancia + 1) > getPainelJogo().getNumeroLinhas())
            ymax = getPainelJogo().getNumeroLinhas();
        else
            ymax = ypac + distancia + 1;
        
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
