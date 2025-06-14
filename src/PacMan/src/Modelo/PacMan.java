package Modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.LeitorTeclado;
import main.PainelJogo;

public class PacMan {
    private int posicaoX;
    private int posicaoY;
    private int velocidade;
    private int estadoBoca;
    private int contadorSprite;
    private BufferedImage cima, baixo, esquerda, direita, repouso;
    private String direcao;

    private PainelJogo painelJogo;
    private LeitorTeclado leitor;

    

    public PacMan(PainelJogo painelJogo, LeitorTeclado leitor) {
        setX(painelJogo.getTamanhoTile());   
        setY(painelJogo.getTamanhoTile());
        velocidade = 5;
        direcao = "direita";

        getImagem();
        
        this.painelJogo = painelJogo;
        this.leitor = leitor;
    }

    public void atualizar() {
        if(leitor.cimaPressionado) {
            direcao = "cima";  
        }

        else if(leitor.direitaPressionado) {
            direcao = "direita";
        }

        else if(leitor.esquerdaPressionado) {
            direcao = "esquerda";
        }

        else if(leitor.baixoPressionado) {
            direcao = "baixo";
        }

        switch (direcao) {
            case "cima":
                if((getY() - painelJogo.getTamanhoTile()/2) > velocidade)
                    setY(getY() - getVelocidade());
                else
                    setY(painelJogo.getTamanhoTile()/2);
                break;

            case "baixo":
                if((getY() + painelJogo.getTamanhoTile()/2) < (painelJogo.getAltura() - velocidade))
                    setY(getY() + getVelocidade());
                else 
                    setY(painelJogo.getAltura() - painelJogo.getTamanhoTile() / 2);
                break;

            case "esquerda":
                if((getX() - painelJogo.getTamanhoTile()/2) > velocidade)
                    setX(getX() - getVelocidade());
                else
                    setX(painelJogo.getTamanhoTile()/2);
                break;

            case "direita":
                if((getX() + painelJogo.getTamanhoTile()/2) < (painelJogo.getLargura() - velocidade))
                    setX(getX() + getVelocidade());
                else
                    setX(painelJogo.getLargura() - painelJogo.getTamanhoTile()/2);
                break;
        }

        contadorSprite++;

        if(contadorSprite > 10) {
            contadorSprite = 0;
            if(estadoBoca == 1)
                estadoBoca = 0;
            else 
                estadoBoca = 1;
        }
    }

    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = null;

        if(estadoBoca == 0) {
            imagem = repouso;
        } else {
            switch (direcao) {
                case "cima":
                    imagem = cima;
                    break;

                case "baixo":
                    imagem = baixo;
                    break;

                case "esquerda":
                    imagem = esquerda;
                    break;

                case "direita":
                    imagem = direita;
                    break;
            }
        }
        caneta.drawImage(imagem, getX()  - (painelJogo.getTamanhoTile())/2, getY() - (painelJogo.getTamanhoTile())/2, painelJogo.getTamanhoTile(), painelJogo.getTamanhoTile(), null);
    }

    public void getImagem() {
        try {
            // Substituir por sprites com transparência
            cima = ImageIO.read(new File("resources\\Pac_Man_sprite_cima.png")); 
            baixo = ImageIO.read(new File("resources\\Pac_Man_sprite_baixo.png"));
            direita = ImageIO.read(new File("resources\\Pac_Man_sprite_direita.png"));
            esquerda = ImageIO.read(new File("resources\\Pac_Man_sprite_esquerda.png"));
            repouso = ImageIO.read(new File("resources\\Pac_Man_sprite_boca_fechada.png"));

        } catch (IOException e) {
            System.err.println("Não foi possível carregar os sprites");
        }
    }

    public void setX(int x) {
        posicaoX = x;
    }

    public void setY(int y) {
        posicaoY = y;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getX() {
        return posicaoX;
    }

    public int getY() {
        return posicaoY;
    }
}
