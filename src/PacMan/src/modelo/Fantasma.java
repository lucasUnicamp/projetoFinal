package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import main.PainelJogo;

public class Fantasma extends Entidade implements Serializable {
    private int estadoPerseguicao; // 1 se o fantasma estiver perseguindo o pacman e 0 caso esteja no modo dispersando
    private int metaCaminho;
    private transient BufferedImage fantasma, fugindo, olhos;
    private transient ArrayList<Ponto> caminhoAtual;
    private int correcoesPendentes;

    // 'comestivel' para quando o pacman comer uma super fruta; talvez fazer com que o pacman mude uma variável do painelJogo e o fantasma acesse ela  
    private boolean comestivel;

    public Fantasma(PainelJogo painel) {
        super(painel);
        correcoesPendentes = 0;
        caminhoAtual = new ArrayList<>();
        metaCaminho = 0;
        estadoPerseguicao = 0;

        setVelocidade((70 * getPainelJogo().getEscala()) / getPainelJogo().getFPS()); 

        getImagem();
    }

    public Fantasma(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        correcoesPendentes = 0;
        caminhoAtual = new ArrayList<>();
        metaCaminho = 0;
        estadoPerseguicao = 1;

        getImagem();
    }

    public void getImagem() {
        try {
            fantasma = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVermelho.png").toString()));
            fugindo = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaFoge.png").toString()));
            olhos = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaOlhos.png").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO FANTASMA !!!");
        }
    }

    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = fantasma;
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    }

    public int calculaDistancia(int x1, int y1, int x2, int y2){
        //retorna o valor numerico da distancia entre dois pontos caso fosse possivel executar o percurso sem colisao
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

    private void adicionarPonto(ArrayList<Ponto> busca, Ponto atual, int x, int y, int d){
        boolean estava = false;
        for(Ponto p: busca){
            if(p.getX() == x && p.getY() == y){
                estava = true;
                if(p.getDistancia() >= d){
                    p.setDistancia(d);
                    p.setPai(atual);
                }
            }
        }
        if(!estava)
            busca.add(new Ponto(x, y, 0, d, atual));
    }

    private boolean jaVisitado(ArrayList<Ponto> visitados, int x, int y){
        for(Ponto p: visitados){
            if(p.getX() == x && p.getY() == y){
                return true;
            }
        }
        return false;
    }

    private void posicoesAdjacentes(Ponto atual, int x2, int y2, ArrayList<Ponto> busca, ArrayList<Ponto> visitados){
        //subida
        int x1 = atual.getX();
        int y1 = atual.getY();
        Elemento mapa[][] = getPainelJogo().elementos;
        try {
            if(!mapa[y1 - 1][x1].ehColidivel() && !jaVisitado(visitados, x1, y1 - 1)){
                int distanciasubida = calculaDistancia(x1, y1 - 1, x2, y2);
                adicionarPonto(busca, atual, x1, y1 - 1, distanciasubida);        
            }
            //casos em que o fantasma seguiu o pac man ate uma posicao de borda

            //descida
            if(!mapa[y1 + 1][x1].ehColidivel() && !jaVisitado(visitados, x1, y1 + 1)){
                int distanciadescida = calculaDistancia(x1, y1 + 1, x2, y2);
                adicionarPonto(busca, atual, x1, y1 + 1, distanciadescida);
            }
            //direita
            if(!mapa[y1][x1 + 1].ehColidivel() && !jaVisitado(visitados, x1 + 1, y1)){
                int distanciadireita = calculaDistancia(x1 + 1, y1, x2, y2);
                adicionarPonto(busca, atual, x1 + 1, y1, distanciadireita);
            }
            //esquerda
            if(!mapa[y1][x1 - 1].ehColidivel() && !jaVisitado(visitados, x1 - 1, y1)){
                int distanciaesquerda = calculaDistancia(x1 - 1, y1, x2, y2);
                adicionarPonto(busca, atual, x1 - 1, y1, distanciaesquerda);
                }
            
            Collections.sort(busca, Comparator.comparingInt(Ponto::getHeuristica));
        } catch (IndexOutOfBoundsException e){

        }
    }

    public void montarCaminho(Ponto destino){
        Ponto aux = destino;
        while(aux != null){
            caminhoAtual.add(0, aux);
            aux = aux.getPai();
        }
    }

    public void dispersar(int xf, int yf){
        //usara a funcao buscar ponto de acordo com o tipo do fantasma quando este estiver no modo dispersao
    }

    public void menorCaminho(int x, int y){
        int xm = x/getPainelJogo().getTamanhoTile();
        int ym = y/getPainelJogo().getTamanhoTile();
        ArrayList<Ponto> visitados = new ArrayList<>();
        ArrayList<Ponto> abertos = new ArrayList<>();

        Ponto inicio = new Ponto(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile(), 0,  calculaDistancia(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile(), xm, ym), null);
        abertos.add(inicio);


        while(!abertos.isEmpty()){
            Collections.sort(abertos, Comparator.comparingInt(Ponto::getHeuristica));
            Ponto atual = abertos.get(0);
            visitados.add(atual);
            if(atual.getX() == xm && atual.getY() == ym){
                caminhoAtual = new ArrayList<>();
                montarCaminho(atual);
                caminhoAtual.remove(0);
                if(caminhoAtual.size() > 5)
                    metaCaminho = 5;
                else
                    metaCaminho = 1;
                return;
                //chegou ao fim
            }
            if(abertos.isEmpty()){ //nao encontrou caminho
                return;
            }
            posicoesAdjacentes(atual, xm, ym, abertos, visitados); //busca pelas posicoes adjacentes tentando avancar ate o destino
            abertos.remove(atual);
        }
    }

    int correcaoPosicao(int x1, int x2){
        if(x2 > x1)
            return (x2 - x1);
        else
            return (x1 - x2);
    }

    public void perseguir(int x, int y){
        //usara a funcao buscar ponto de acordo com o tipo do fantasma quando este estiver perseguindo o pacman

    }

    public void buscarPonto(){
        //posicoes iniciais do fantasma na matriz
        int xm = getX()/getPainelJogo().getTamanhoTile();
        int ym = getY()/getPainelJogo().getTamanhoTile();
        Ponto proximo = null;
        if(caminhoAtual.size() > 0) 
            proximo = caminhoAtual.get(0);
        
        if(proximo != null && (proximo.getX() != xm || proximo.getY() != ym)){
            //o fantasma esta no meio do caminho
            if(xm == proximo.getX()){
                if(getDirecao().equals("direita") || getDirecao().equals("esquerda")){
                    correcoesPendentes = correcaoPosicao(getX(), getPainelJogo().getTamanhoTile() * xm + getPainelJogo().getTamanhoTile()/2);
                    if(correcoesPendentes > 0){
                        correcoesPendentes-= getVelocidade();
                        if(getDirecao().equals("direita"))
                            setX(getX() + getVelocidade());
                        else
                            setX(getX() - getVelocidade());
                        return;
                    }
                }
                //a proxima posicao no caminho varia em y: fantasma movera para cima ou para baixo
                if (ym < proximo.getY()) {
                    setDirecao("baixo");
                    setY(getY() + getVelocidade());
                }
                else{
                    setDirecao("cima");
                    setY(getY() - getVelocidade());
                }
                ym = getY()/getPainelJogo().getTamanhoTile();
            }
            else if (ym == proximo.getY()){
                //a proxima posicao no caminho varia em x: fantasma movera para direita ou esquerda
                if(getDirecao().equals("cima") || getDirecao().equals("baixo")){
                    correcoesPendentes = correcaoPosicao(getY(), getPainelJogo().getTamanhoTile() * ym + getPainelJogo().getTamanhoTile()/2);
                    if(correcoesPendentes > 0){
                        correcoesPendentes-= getVelocidade();
                        if(getDirecao().equals("cima"))
                            setY(getY() - getVelocidade());
                        else
                            setY(getY() + getVelocidade());
                        return;
                    }  
                }
                if(xm < proximo.getX()) {
                    setDirecao("direita");
                    setX(getX() + getVelocidade());
                }
                else{
                    setDirecao("esquerda");
                    setX(getX() - getVelocidade());
                }
                xm = getX()/getPainelJogo().getTamanhoTile();
            }

            if(proximo.getX() == xm && proximo.getY() == ym){
                //fantasma avancou uma posicao na matriz
                caminhoAtual.remove(0);
                metaCaminho--;

            }
        }
    }

    public void voltarSpawn() {
        // Para quando for comido, deve voltar à posição inicial, sair do modo 'morto' e voltar a perseguir o pacman
    }

    public void executarfuncao(int x, int y){
        //coordenadas do fantasma na matriz
        int xf = getX()/getPainelJogo().getTamanhoTile();
        int yf = getY()/getPainelJogo().getTamanhoTile();
        //coordenadas do destino na matriz
        int xm = x/getPainelJogo().getTamanhoTile();
        int ym = y/getPainelJogo().getTamanhoTile();


        //perseguir(x, y): caso o fantasma e seu destino nao estejam no mesmo ponto da matriz
        if(xf != xm || yf != ym){
            if(correcoesPendentes > 0){
                if(getDirecao().equals("direita"))
                    setX(getX() + getVelocidade());
                else  if(getDirecao().equals("esquerda"))
                    setX(getX() - getVelocidade());
                else if(getDirecao().equals("cima"))
                    setY(getY() - getVelocidade());
                else if(getDirecao().equals("baixo"))
                    setY(getY() + getVelocidade());
                correcoesPendentes-= getVelocidade();
                if(correcoesPendentes <= 0){
                    setSpawn(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile());
                }
                return;
            }
            if(metaCaminho == 0)
                menorCaminho(x, y);
            buscarPonto();
        }  
    }

    public int getEstadoPerseguicao() {
        return estadoPerseguicao;
    }
}
