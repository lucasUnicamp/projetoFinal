package modelo;

import interfaces.Elemento;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

import enums.EstadoPerseguicao;
import main.PainelJogo;

public final class FantasmaVerde extends Fantasma{
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
        if(getCorrecoesPendentes() > 0){
            corrigirPosicao(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile());
            return;
        }
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
            melhorCaminho(cantos.get(prox).getX(), cantos.get(prox).getY());
            setMetaCaminho(getCaminhoatual().size());
            buscarPonto();
        }
    }

    @Override
    public void executarfuncao(){
        if(getEstadoPerseguicao() == EstadoPerseguicao.PERSEGUINDO){
            funcaoPerseguicao();
        }
        else if (getEstadoPerseguicao() == EstadoPerseguicao.DISPERSO){
            funcaoMovimento(getPainelJogo().getPacMan().getX()/getPainelJogo().getTamanhoTile(), getPainelJogo().getPacMan().getY()/getPainelJogo().getTamanhoTile());
        } else if(getEstadoPerseguicao() == EstadoPerseguicao.MORTO) {
            funcaoMovimento(getXInicial()/getPainelJogo().getTamanhoTile(), getYInicial()/getPainelJogo().getTamanhoTile());
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
