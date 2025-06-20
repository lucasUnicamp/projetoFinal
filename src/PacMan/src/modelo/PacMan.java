package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import interfaces.Elemento;
import main.LeitorTeclado;
import main.PainelJogo;

public class PacMan extends Entidade {
    private int estadoBoca; // boca aberta ou fechada
    private int contadorSprite; // usado para saber quando mudar o sprite (animação)
    private BufferedImage cima, baixo, esquerda, direita, repouso;
    private String direcaoDesejada;

    private LeitorTeclado leitor;

    public PacMan(PainelJogo painelJogo, LeitorTeclado leitor) {
        super(painelJogo);

        getImagem();

        this.leitor = leitor;
    }

    // atualiza o estado do pac-man dependo do input do usuário
    public void atualizar() {
        if (leitor.cimaPressionado) {
            setDirecaoDesejada("cima");
        }
        else if (leitor.direitaPressionado) {
            setDirecaoDesejada("direita");
        }

        else if (leitor.esquerdaPressionado) {
            setDirecaoDesejada("esquerda");
        }

        else if (leitor.baixoPressionado) {
            setDirecaoDesejada("baixo");
        }

        int linhaMatriz = getY()/getPainelJogo().getTamanhoTile();
        int colunaMatriz = getX()/getPainelJogo().getTamanhoTile();
        Elemento[][] matrizElementos = getPainelJogo().elementos;

        try {
            if ((getDirecao() == "cima" || getDirecao() == "baixo"))
                if (getDirecaoDesejada() == "direita") {
                    if(!matrizElementos[linhaMatriz][colunaMatriz + 1].ehColidivel())
                        setDirecao("direita");
                } else if (getDirecaoDesejada() == "esquerda") {
                    if(!matrizElementos[linhaMatriz][colunaMatriz - 1].ehColidivel())
                        setDirecao("esquerda");
                } else {
                    setDirecao(getDirecaoDesejada());
                }
            else if((getDirecao() == "direita" || getDirecao() == "esquerda"))
                if (getDirecaoDesejada() == "cima") {
                    if(!matrizElementos[linhaMatriz - 1][colunaMatriz].ehColidivel())
                        setDirecao("cima");
                } else if (getDirecaoDesejada() == "baixo") {
                    if(!matrizElementos[linhaMatriz + 1][colunaMatriz].ehColidivel())
                        setDirecao("baixo");
                } else {
                    setDirecao(getDirecaoDesejada());
                }

        } catch (ArrayIndexOutOfBoundsException  e) {
            setDirecao(getDirecaoDesejada());
        }

        mover();

        Elemento elemento = matrizElementos[linhaMatriz][colunaMatriz];

        if(elemento instanceof Comestivel) {
            ((Comestivel)elemento).comer();
        }

        contadorSprite++;

        if (contadorSprite > 10) { // muda o sprite após um certo número de quadros
            contadorSprite = 0;
            if (estadoBoca == 1)
                estadoBoca = 0;
            else 
                estadoBoca = 1;
        }
    }

    // instruções para desenhar o pac-man na tela
    public void desenhar(Graphics2D caneta) {
        BufferedImage imagem = null;

        if (estadoBoca == 0) {
            imagem = repouso;
        } else {
            switch (getDirecao()) {
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
        caneta.drawImage(imagem, getX()  - (getPainelJogo().getTamanhoTile())/2, getY() - (getPainelJogo().getTamanhoTile())/2, getPainelJogo().getTamanhoTile(), getPainelJogo().getTamanhoTile(), null);
    }

    public void setDirecaoDesejada(String direcao) {
        if(direcao == "direita" || direcao == "esquerda" || direcao == "cima" || direcao == "baixo") 
            this.direcaoDesejada = direcao;
    }

    public String getDirecaoDesejada() {
        return direcaoDesejada;
    }

    // importa os sprites
    public void getImagem() {
        try {
            // Substituir por sprites com transparência
            cima = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanNorte.png").toString())); 
            baixo = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanSul.png").toString()));
            direita = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanLeste.png").toString()));
            esquerda = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanOeste.png").toString()));
            repouso = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanParado.png").toString()));

        } catch (IOException e) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO PACMAN !!!");
        }
    }
}
