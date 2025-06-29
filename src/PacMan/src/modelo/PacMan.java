package modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import interfaces.Elemento;
import main.LeitorTeclado;
import main.PainelJogo;

public class PacMan extends Entidade{
    private int estadoBoca; // boca aberta ou fechada
    private int contadorSprite; // usado para saber quando mudar o sprite (animação)
    private transient BufferedImage cima, baixo, esquerda, direita, repouso, dano;
    private String direcaoDesejada;
    private Boolean estaMorto;

    private int vidas;

    private transient LeitorTeclado leitor;

    public PacMan(PainelJogo painelJogo, LeitorTeclado leitor) {
        super(painelJogo);
        setEstaMorto(false);
        vidas = 3;

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
        setDimensoesHitBox(getPainelJogo().getTamanhoTile()/2);

        try {
            if(getDirecaoDesejada() != null) {
                if (getDirecao().equals("cima") || getDirecao().equals("baixo"))
                    if (getDirecaoDesejada().equals("direita")) {
                        if(!matrizElementos[linhaMatriz][colunaMatriz + 1].ehColidivel() && Math.abs(getY() - linhaMatriz*getPainelJogo().getTamanhoTile() - getPainelJogo().getTamanhoTile()/2) <= getVelocidade())
                            setDirecao("direita");
                    } else if (getDirecaoDesejada().equals("esquerda")) {
                        if(!matrizElementos[linhaMatriz][colunaMatriz - 1].ehColidivel() && Math.abs(getY() - linhaMatriz*getPainelJogo().getTamanhoTile() - getPainelJogo().getTamanhoTile()/2) <= getVelocidade())
                            setDirecao("esquerda");
                    } else {
                        setDirecao(getDirecaoDesejada());
                        setDimensoesHitBox(getPainelJogo().getTamanhoTile() - 1);
                    }
                else if(getDirecao().equals("direita") || getDirecao().equals("esquerda"))
                    if (getDirecaoDesejada().equals("cima")) {
                        if(!matrizElementos[linhaMatriz - 1][colunaMatriz].ehColidivel() && Math.abs(getX() - colunaMatriz*getPainelJogo().getTamanhoTile() - getPainelJogo().getTamanhoTile()/2) <= getVelocidade())
                            setDirecao("cima");
                    } else if (getDirecaoDesejada().equals("baixo")) {
                        if(!matrizElementos[linhaMatriz + 1][colunaMatriz].ehColidivel() && Math.abs(getX() - colunaMatriz*getPainelJogo().getTamanhoTile() - getPainelJogo().getTamanhoTile()/2) <= getVelocidade())
                            setDirecao("baixo");
                    } else {
                        setDirecao(getDirecaoDesejada());
                        setDimensoesHitBox(getPainelJogo().getTamanhoTile() - 1);
                    }
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

        if (contadorSprite > 5) { // muda o sprite após um certo número de quadros
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

        if (getEstaMorto()) {
            imagem = dano;
        } else if (estadoBoca == 0) {
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
        if(direcao != null && (direcao.equals("direita") || direcao.equals("esquerda") || direcao.equals("cima") || direcao.equals("baixo"))) 
            this.direcaoDesejada = direcao;
    }

    public void setVidas(int vidas) {
        if(vidas <= 3 && vidas >= 0) {
            this.vidas = vidas;
        }
    }

    public void morrer() {
        setEstaMorto(true);
        setVidas(getVidas() - 1);
    }

    public void setEstaMorto(Boolean estaMorto) {
        this.estaMorto = estaMorto;
    }

    public String getDirecaoDesejada() {
        return direcaoDesejada;
    }

    public int getVidas() {
        return vidas;
    }

    public Boolean getEstaMorto() {
        return estaMorto;
    }

    // importa os sprites
    public void getImagem() {
        try {
            cima = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanNorte.png").toString())); 
            baixo = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanSul.png").toString()));
            direita = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanLeste.png").toString()));
            esquerda = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanOeste.png").toString()));
            repouso = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanParado.png").toString()));
            dano = ImageIO.read(new File(Paths.get("resources", "imagens", "pacmanDano.png").toString()));

        } catch (IOException e) {
            System.err.println("!!! ERRO NA IMPORTAÇÃO DOS SPRITES DO PACMAN !!!");
        }
    }

    public BufferedImage getImagemRepouso() {
        return repouso;
    }
}
