package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import enums.TipoFantasma;
import interfaces.Elemento;
import modelo.Fantasma;
import modelo.FantasmaRosa;
import modelo.FantasmaVerde;
import modelo.FantasmaVermelho;

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
            dados.mapaAtual = painelJogo.getTratadorMapa().getMapaEscolhido();
            dados.numeroLinhas = painelJogo.getNumeroLinhas();
            dados.numeroColunas = painelJogo.getNumeroColunas();
            dados.pontuacao = painelJogo.getPontuacao();
            dados.xPacMan = painelJogo.getPacMan().getX();
            dados.yPacMan = painelJogo.getPacMan().getY();
            dados.xInicialPacMan = painelJogo.getPacMan().getXInicial();
            dados.yInicialPacMan = painelJogo.getPacMan().getYInicial();
            dados.direcaoPacMan = painelJogo.getPacMan().getDirecao();

            dados.tiposFantasmas = new ArrayList<>();
            dados.xFantasmas = new ArrayList<>();
            dados.yFantasmas = new ArrayList<>();
            dados.direcaoFantasmas = new ArrayList<>();
            dados.xInicialFantasmas = new ArrayList<>();
            dados.yInicialFantasmas = new ArrayList<>();

            for(int i = 0; i < painelJogo.fantasmas.size(); i++) {
                Fantasma fantasmaAtual = painelJogo.fantasmas.get(i);
                if(fantasmaAtual instanceof FantasmaRosa) {
                    dados.tiposFantasmas.add(TipoFantasma.ROSA);
                } else if(fantasmaAtual instanceof FantasmaVerde) {
                    dados.tiposFantasmas.add(TipoFantasma.VERDE);
                } else if(fantasmaAtual instanceof FantasmaVermelho) {
                    dados.tiposFantasmas.add(TipoFantasma.VERMELHO);
                }
                dados.xFantasmas.add(fantasmaAtual.getX());
                dados.yFantasmas.add(fantasmaAtual.getY());
                dados.direcaoFantasmas.add(fantasmaAtual.getDirecao());
                dados.xInicialFantasmas.add(fantasmaAtual.getXInicial());
                dados.yInicialFantasmas.add(fantasmaAtual.getYInicial());
            }
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

            painelJogo.setTratadorMapa(new TratadorMapa(dados.mapaAtual));
            painelJogo.setMapa(dados.mapa);
            painelJogo.setNumeroLinhas(dados.numeroLinhas);
            painelJogo.setNumeroColunas(dados.numeroColunas);
            painelJogo.elementos = new Elemento[dados.numeroLinhas][dados.numeroColunas];
            painelJogo.carregarElementos();
            painelJogo.getPacMan().setVidas(dados.vidasPacMan);
            painelJogo.setPontuacao(dados.pontuacao);
            painelJogo.getPacMan().setX(dados.xPacMan);
            painelJogo.getPacMan().setY(dados.yPacMan);
            painelJogo.getPacMan().setXInicial(dados.xInicialPacMan);
            painelJogo.getPacMan().setYInicial(dados.yInicialPacMan);
            painelJogo.getPacMan().setDirecao(dados.direcaoPacMan.toString());
            

            for(int i = 0; i < dados.tiposFantasmas.size(); i++) {
                switch (dados.tiposFantasmas.get(i)) {
                    case VERDE:
                        painelJogo.fantasmas.add(new FantasmaVerde(painelJogo));
                        break;
                    case ROSA:
                        painelJogo.fantasmas.add(new FantasmaRosa(painelJogo));
                        break;
                    case VERMELHO:
                        painelJogo.fantasmas.add(new FantasmaVermelho(painelJogo));
                        break;
                }
                painelJogo.fantasmas.get(i).setX(dados.xFantasmas.get(i));
                painelJogo.fantasmas.get(i).setY(dados.yFantasmas.get(i));
                painelJogo.fantasmas.get(i).setXInicial(dados.xInicialFantasmas.get(i));
                painelJogo.fantasmas.get(i).setYInicial(dados.yInicialFantasmas.get(i));
                painelJogo.fantasmas.get(i).setDirecao(dados.direcaoFantasmas.get(i).toString());
            }
            


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
