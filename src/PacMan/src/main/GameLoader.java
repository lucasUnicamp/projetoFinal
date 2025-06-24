package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import interfaces.Elemento;

public class GameLoader {

    private PainelJogo painelJogo;

    public GameLoader(PainelJogo painelJogo) {
        this.painelJogo = painelJogo;
    }

    public void salvar() {
        try {
            ObjectOutputStream arquivoSave = new ObjectOutputStream(new FileOutputStream(Paths.get("resources", "saves", "save0.dat").toString()));
            
            DadosJogo dados = new DadosJogo();
            
            String[] mapa = new String[painelJogo.getMapa().length];

            for(int i = 0; i < painelJogo.elementos.length; i++) {
                mapa[i] = "";
                for(Elemento elemento : painelJogo.elementos[i]) {
                    char representacao = elemento.getRepresentacao();
                    if(representacao != 'P' && representacao != 'R')
                        mapa[i] = mapa[i] + representacao;
                    else
                        mapa[i] = mapa[i] + " ";
                }
            }

            dados.mapa = mapa;
            dados.pontuacao = painelJogo.getPontuacao();
            dados.xPacMan = painelJogo.getPacMan().getX();
            dados.yPacMan = painelJogo.getPacMan().getY();
            dados.xInicialPacMan = painelJogo.getPacMan().getXInicial();
            dados.yInicialPacMan = painelJogo.getPacMan().getYInicial();
            dados.direcaoPacMan = painelJogo.getPacMan().getDirecao();
            dados.xFantasma = painelJogo.getFantasma().getX();
            dados.yFantasma = painelJogo.getFantasma().getY();
            dados.xInicialFantasma = painelJogo.getPacMan().getXInicial();
            dados.yInicialFantasma = painelJogo.getPacMan().getYInicial();
            dados.direcaoFantasma = painelJogo.getFantasma().getDirecao();
            dados.vidasPacMan = painelJogo.getPacMan().getVidas();

            arquivoSave.writeObject(dados);
            arquivoSave.flush();
            arquivoSave.close();

        } catch (IOException e) {
            System.err.println("Erro ao salvar o jogo");
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            ObjectInputStream arquivoSave = new ObjectInputStream(new FileInputStream(Paths.get("resources", "saves", "save0.dat").toString()));
            DadosJogo dados = (DadosJogo) arquivoSave.readObject();

            painelJogo.setMapa(dados.mapa);
            painelJogo.carregarElementos();
            painelJogo.getPacMan().setVidas(dados.vidasPacMan);
            painelJogo.setPontuacao(dados.pontuacao);
            painelJogo.getPacMan().setX(dados.xPacMan);
            painelJogo.getPacMan().setY(dados.yPacMan);
            painelJogo.getPacMan().setXInicial(dados.xInicialPacMan);
            painelJogo.getPacMan().setYInicial(dados.yInicialPacMan);
            painelJogo.getPacMan().setDirecao(dados.direcaoPacMan.toString());
            painelJogo.getFantasma().setX(dados.xFantasma);
            painelJogo.getFantasma().setY(dados.yFantasma);
            painelJogo.getPacMan().setXInicial(dados.xInicialFantasma);
            painelJogo.getPacMan().setYInicial(dados.yInicialFantasma);
            painelJogo.getFantasma().setDirecao(dados.direcaoFantasma.toString());
            


            arquivoSave.close();

        } catch (IOException e) {
            System.err.println("Erro ao carregar o jogo!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o jogo!");
            e.printStackTrace();
        }
    }
}
