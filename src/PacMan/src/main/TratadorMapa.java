package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.nio.file.Paths;

public class TratadorMapa {
    private int mapaEscolhido;
    private File mapaArquivo;

    public TratadorMapa(int mapaEscolhido) {
        this.mapaEscolhido = mapaEscolhido;
        carregarMapa();
    }

    public void carregarMapa() {
        String mapa = "mapa" + Integer.toString(getMapaEscolhido());
        File mapaArquivo = new File(Paths.get("resources", "mapas", mapa).toString());

        if (mapaArquivo.isFile()) {
            System.out.printf("Mapa %d carregado com sucesso.\n", mapaEscolhido);
            checarMapa();
            setMapaArquivo(mapaArquivo);
        }
        else {
            System.out.println("Nenhum mapa encontrado. Configuração genérica será usada.");
            criarGenerico();
            setMapaArquivo(new File(Paths.get("resources", "mapas", "mapa0").toString()));
        }
    }

    public void criarGenerico() {
        try {
            Formatter arquivoLog  = new Formatter(new FileWriter(Paths.get("resources", "mapas", "mapa0").toString(), false));
            arquivoLog.format("ppppppppppcppppppppp\n" +
                              "pccccccccccccccccccp\n" + 
                              "pcppppppppcppcppppcp\n" + 
                              "pcppppppppcppcppppcp\n" + 
                              "pccccccccccppcppppcp\n" + 
                              "pcppppppcpcppcppppcp\n" + 
                              "ccppppppcpcppcppppcp\n" + 
                              "pcppppcccccccccpppcp\n" + 
                              "pcppppccpppppccpppcc\n" + 
                              "pcppppccpppppccccccp\n" + 
                              "pcppppccpppppccpppcp\n" + 
                              "pcppppcccccccccpppcp\n" + 
                              "pcppppppppcpppppppcp\n" + 
                              "pcppppppppcpppppppcp\n" + 
                              "pcppppppppccccccppcp\n" + 
                              "pcpppppppppppppcppcp\n" + 
                              "pccccccccccccccccccp\n" + 
                              "pcpppcpppppppppcppcp\n" + 
                              "pccccccccccccccccccp\n" + 
                              "pppppppppppcpppppppp");
            arquivoLog.flush();
            arquivoLog.close();
        } catch (IOException erro) {
            System.err.println("!!! ERRO AO CRIAR MAPA GENÉRICO !!!");
        }
    }

    public void checarMapa() {

    }

    public String[] atribuirMapa() {
        ArrayList<String> mapa = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(getMapaArquivo()))) {
            String line;

            while ((line = br.readLine()) != null) {
                mapa.add(line);
            }
        } catch (IOException erro) {
            System.err.println("!!! ARQUIVO NÃO FOI ENCONTRADO PARA SER ATRIBUIDO !!!");
        }
    
        return mapa.toArray(new String[mapa.size()]);
    }

    public int getMapaEscolhido() {
        return mapaEscolhido;
    }

    public File getMapaArquivo() {
        return mapaArquivo;
    }
    
    public void setMapaArquivo(File mapaArquivo) {
        this.mapaArquivo = mapaArquivo;
    }
}
