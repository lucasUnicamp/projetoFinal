package modelo;

public class Direcoes {
    private int distancia;
    private String direcao;

    public Direcoes(int d, String s){
        distancia = d;
        direcao = s;
    }

    public int getDistancia(){
        return distancia;
    }

    public String getDirecao(){
        return this.direcao;
    }
}
