package modelo;

public class Ponto {
    private Ponto pai;
    private int distancia;
    private final Coordenada coordenada;

    public Ponto(int xp, int yp, int d, Ponto origem){
        pai = origem;
        coordenada = new Coordenada(xp, yp);
        distancia = d;
    }

    public int getX(){
        return coordenada.getX();
    }

    public int getY(){
        return coordenada.getY();
    }

    public Ponto getPai(){
        return pai;
    }

    public void setPai(Ponto p){
        pai = p;
    }


    public int getHeuristica(boolean modo){
        if(modo)
            return distancia;
        return - distancia;
    }

    public int getDistancia(){
        return distancia;
    }

    public void setDistancia(int d){
        distancia = d;
    }

    
}
