package modelo;

import interfaces.Elemento;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.Timer;

import enums.EstadoPerseguicao;
import main.PainelJogo;

public abstract class Fantasma extends Entidade {
    private EstadoPerseguicao perseguicao; // 1 se o fantasma estiver perseguindo o pacman e 0 caso esteja no modo dispersando
    private int metaCaminho;
    private transient ArrayList<Ponto> caminhoAtual;
    private int correcoesPendentes;
    private Timer timerParar;
    private int frame = 0;
    BufferedImage imagemFantasma, imagemFugindo, imagemOlhos;

    public Fantasma(PainelJogo painel) {
        super(painel);
        correcoesPendentes = 0;
        caminhoAtual = new ArrayList<>();
        metaCaminho = 0;
        perseguicao = EstadoPerseguicao.PERSEGUINDO;
        setVelocidade((60 * getPainelJogo().getEscala()) / getPainelJogo().getFPS()); 
        setVelocidadePadrao(getVelocidade());
    }

    public Fantasma(PainelJogo painel, int x, int y, int velocidade, String direcao) {
        super(painel, x, y, velocidade, direcao);
        correcoesPendentes = 0;
        caminhoAtual = new ArrayList<>();
        metaCaminho = 0;
        perseguicao = EstadoPerseguicao.PERSEGUINDO;
    }

    public int getMetaCaminho(){
        return metaCaminho;
    }

    public EstadoPerseguicao getEstadoPerseguicao(){
        return perseguicao;
    }

    public int getCorrecoesPendentes(){
        return correcoesPendentes;
    }

    public void setCorrecoesPendentes(int x){
        correcoesPendentes = x;
    }

    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = imagemFantasma;
        switch (getEstadoPerseguicao()) {
            case PERSEGUINDO:
                imagem = imagemFantasma;
                break;
            case DISPERSO:
                imagem = imagemFugindo;
                break;
            case MORTO:
                imagem = imagemOlhos;
                break;
        }
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    };

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
            busca.add(new Ponto(x, y, d, atual));
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
        } catch (IndexOutOfBoundsException e){}
        try{
            //descida
            if(!mapa[y1 + 1][x1].ehColidivel() && !jaVisitado(visitados, x1, y1 + 1)){
                int distanciadescida = calculaDistancia(x1, y1 + 1, x2, y2);
                adicionarPonto(busca, atual, x1, y1 + 1, distanciadescida);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            //direita
            if(!mapa[y1][x1 + 1].ehColidivel() && !jaVisitado(visitados, x1 + 1, y1)){
                int distanciadireita = calculaDistancia(x1 + 1, y1, x2, y2);
                adicionarPonto(busca, atual, x1 + 1, y1, distanciadireita);
            }
        } catch (IndexOutOfBoundsException e){}  
        try{
            //esquerda
            if(!mapa[y1][x1 - 1].ehColidivel() && !jaVisitado(visitados, x1 - 1, y1)){
                int distanciaesquerda = calculaDistancia(x1 - 1, y1, x2, y2);
                adicionarPonto(busca, atual, x1 - 1, y1, distanciaesquerda);
            }
        } catch (IndexOutOfBoundsException e){}
        Collections.sort(busca, Comparator.comparingInt(ponto -> ponto.getHeuristica(this.getEstadoPerseguicao().ehHeuristicaPositiva())));
    }

    public void montarCaminho(Ponto destino){
        Ponto aux = destino;
        while(aux != null){
            caminhoAtual.add(0, aux);
            aux = aux.getPai();
        }
    }

    public ArrayList<Ponto> getCaminhoatual(){
        return caminhoAtual;
    }

    public void melhorCaminho(int x, int y){
        ArrayList<Ponto> visitados = new ArrayList<>();
        ArrayList<Ponto> abertos = new ArrayList<>();

        Ponto inicio = new Ponto(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile(),  calculaDistancia(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile(), x, y), null);
        abertos.add(inicio);


        while(!abertos.isEmpty()){
            Collections.sort(abertos, Comparator.comparingInt(ponto -> ponto.getHeuristica(this.getEstadoPerseguicao().ehHeuristicaPositiva())));

            Ponto atual = abertos.get(0);
            visitados.add(atual);
            if(atual.getX() == x && atual.getY() == y){
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
            posicoesAdjacentes(atual, x, y, abertos, visitados); //busca pelas posicoes adjacentes tentando avancar ate o destino
            abertos.remove(atual);
        }
    }

    public int correcaoPosicao(int x1, int x2){
        if(x2 > x1)
            return (x2 - x1);
        else
            return (x1 - x2);
    }

    public void setMetaCaminho(int x){
        metaCaminho = x;
    }

    public void corrigirPosicao(int xf, int yf){
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
    }

    public boolean naoehVizinho(int xf, int yf){
        return (xf != caminhoAtual.get(0).getX() && yf != caminhoAtual.get(0).getY());
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

    public void perder(){
        setEstadoPerseguicao(EstadoPerseguicao.MORTO);
        melhorCaminho(getXInicial()/getPainelJogo().getTamanhoTile(), getYInicial()/getPainelJogo().getTamanhoTile());
        metaCaminho = caminhoAtual.size();
        
    }

    public void resetar(){
        setVelocidade((60 * getPainelJogo().getEscala()) / getPainelJogo().getFPS());
        metaCaminho = 0;
    }

    public void acionarFuga(){
        setEstadoPerseguicao(EstadoPerseguicao.DISPERSO);
        metaCaminho = 0;
    }

    public void funcaoMovimento(int xm, int ym){
        //implementamos a fuga da mesma maneira que implementamos a busca: o mesmo algoritmo foi implementado
        //a diferenca eh que na busca o fantasma vermelho busca o menor caminho ate o pacman (perseguicao)
        //na fuga, por sua vez, o fantasma esta buscando o maior caminho ate o pacman, que atualizado repetidamente se assemelha a uma fuga
        //xm e ym sao coordenadas destino dos fantasmas
    
        //coordenadas do fantasma na matriz
        int xf = getX()/getPainelJogo().getTamanhoTile();
        int yf = getY()/getPainelJogo().getTamanhoTile();
        //perseguir(x, y): caso o fantasma e seu destino nao estejam no mesmo ponto da matriz
        if(getCorrecoesPendentes() > 0){
            corrigirPosicao(xf, yf);
            return;
        }
        if(getMetaCaminho() == 0 || caminhoAtual.isEmpty())
            melhorCaminho(xm, ym);
        else if(!caminhoAtual.isEmpty()){
            if(naoehVizinho(xf, yf))
                melhorCaminho(xm, ym);
        }
        buscarPonto();
        if(getEstadoPerseguicao() == EstadoPerseguicao.MORTO && getX()/getPainelJogo().getTamanhoTile() == xm && getY()/getPainelJogo().getTamanhoTile() == ym){
            encerrarFuga();
        }
    }

    /**
     * Muda o estado do Fantasma de volta para Perseguindo com um delay. Esse delay serve para
     * o fantasma não renascer imediatamente depois de voltar ao spawn
     */
    public void encerrarFuga(){
        if (timerParar == null) {
            timerParar = new Timer(100, e -> {
                frame++;
                if (frame >= 5) {
                    setEstadoPerseguicao(EstadoPerseguicao.PERSEGUINDO);
                    metaCaminho = 0;
                    frame = 0;
                    timerParar.stop();
                }
            });
        }
        
        timerParar.start();
    }

    /**
     * Também muda o estados do Fantasma de volta par Perseguido, mas sem o delay. Serve para
     * quando Pac-Man morre enquanto outros fantasmas estão fugindo
     */
    public void forcarEncerrarFuga() {
        setEstadoPerseguicao(EstadoPerseguicao.PERSEGUINDO);
        metaCaminho = 0;
    }

    public void voltarPosicaoInicial(){
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
                centralizarPorPixel(getX()/getPainelJogo().getTamanhoTile(), getY()/getPainelJogo().getTamanhoTile());
            }
            return;
        }
        if(getX() != getXInicial() && getY() != getYInicial()){
            buscarPonto();
        }
    }

    public abstract void executarfuncao();

    public abstract void getImagem();

    public void setEstadoPerseguicao(EstadoPerseguicao estado) {
        switch (estado) {
            case PERSEGUINDO:
                setVelocidade(getVelocidadePadrao());
                break;
            case DISPERSO:
                setVelocidade((int)(getVelocidadePadrao() * 0.8));
                break;
            case MORTO:
                setVelocidade(2*getVelocidadePadrao());
            default:
                break;
        }
        perseguicao = estado;
    }
}
