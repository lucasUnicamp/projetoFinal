package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import main.PainelJogo;

public class Fantasma extends Entidade{

    private int estadoPerseguicao; //1 se o fantasma estiver perseguindo o pacman e 0 caso esteja no modo dispersando
    private BufferedImage provisoria;

    public Fantasma(PainelJogo painel) {
        super(painel);
        setX(getPainelJogo().getTamanhoTile() * 10 + getPainelJogo().getTamanhoTile()/2);   
        setY(getPainelJogo().getTamanhoTile() * 11 + getPainelJogo().getTamanhoTile()/2);

        estadoPerseguicao = 1;
        setVelocidade((70 * getPainelJogo().getEscala()) / getPainelJogo().getFPS()); 

        setDirecao("cima");

        try {
            provisoria = ImageIO.read(new File(Paths.get("resources", "imagens", "fantasmaVermelho.png").toString()));
        } catch (IOException e) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DO SPRITE DO FANTASMA !!!");
        }
    }

    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = provisoria;
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

    private ArrayList<Direcoes> prioridade(int x, int y){
        //retorna um array ordenado com base na prioridade de direcao para menor distancia ate as coordenadas x e y
        ArrayList<Direcoes> prioridades = new ArrayList<>(); //prioridades no sentido do movimento
        
        //subida
        int distanciasubida = calculaDistancia(getX(), getY() - getVelocidade(), x, y);
        prioridades.add(new Direcoes(distanciasubida, "cima"));
        //descida
        int distanciadescida = calculaDistancia(getX(), getY() + getVelocidade(), x, y);
        prioridades.add(new Direcoes(distanciadescida, "baixo"));
        //direita
        int distanciadireita = calculaDistancia(getX() + getVelocidade(), getY(), x, y);
        prioridades.add(new Direcoes(distanciadireita, "direita"));
        //esquerda
        int distanciaesquerda = calculaDistancia(getX() - getVelocidade(), getY(), x, y);
        prioridades.add(new Direcoes(distanciaesquerda, "esquerda"));
        Collections.sort(prioridades, Comparator.comparingInt(Direcoes::getDistancia));
        return prioridades;

    }

    public void dispersar(int xf, int yf){
        //implementar no futuro quando o fantasma estiver no modo dispersar
    }

    public void perseguir(int x, int y){
        int menorDistantcia = calculaDistancia(this.getX(), this.getY(), x, y);
        //posicoes iniciais
        int xi = getX();
        int yi = getY();
        String dini = this.getDirecao();
        ArrayList<Direcoes> direcoes = prioridade(x, y);

        if(menorDistantcia > direcoes.get(0).getDistancia()){
            //escolha da direcao que apos o movimento mais se aproximara do destino desejado
            for(Direcoes d: direcoes){
                this.setDirecao(d.getDirecao());
                this.mover();
                if(xi != this.getX() || yi != this.getY()){ //moveu
                    break;
                }
            }
        }

    }

    public void executarfuncao(int x, int y){
        this.perseguir(x, y);
    }


}
