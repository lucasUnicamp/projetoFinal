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
    private int mapaLargura;
    private int mapaAltura;
    private File mapaArquivo;

    public TratadorMapa(int mapaEscolhido) {
        this.mapaEscolhido = mapaEscolhido;
        carregarMapa();
    }

    /**
     * Atribui um arquivo de mapa ao objeto. Caso nenhum arquivo exista ou seja inválido, cria um arquivo de mapa
     * genérico para ser usado
     */
    public void carregarMapa() {
        // String do nome do arquivo. O mapa é escolhido em 'PainelJogo'
        String mapa = "mapa" + Integer.toString(getMapaEscolhido());
        File mapaArquivo = new File(Paths.get("resources", "mapas", mapa).toString());

        if (mapaArquivo.isFile()) {
            System.out.printf("Mapa %d carregado com sucesso.\n", mapaEscolhido);
            checarMapa();
        }
        // Mesmo se o 'mapa0' já existir, cria outro por precaução
        else {
            System.out.println("Nenhum mapa encontrado. Configuração genérica será usada.");
            criarGenerico();
            mapaArquivo = new File(Paths.get("resources", "mapas", "mapa0").toString());
        }
        setMapaArquivo(mapaArquivo);
    }

    // Cria o mapa genérico para que o jogo não fique sem nenhum
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

    /**
     * Converte o arquivo de texto em um array de string para ser usado no jogo. também atribui as medidas
     * do mapa às propriedades adequadas
     * @return String[] do mapa baseada no arquivo de mapa
     */
    public String[] atribuirMapa() {
        ArrayList<String> mapa = new ArrayList<String>();
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(getMapaArquivo()))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Vê o tamanho da primeira linha para usar como largura do mapa
                if (contador == 0)
                    setMapaLargura(line.length());
                mapa.add(line);
                contador++;
            }
        } catch (IOException erro) {
            System.err.println("!!! ARQUIVO NÃO FOI ENCONTRADO PARA SER ATRIBUIDO !!!");
        }

        // Usa o número de linhas como altura do mapa
        setMapaAltura(contador);
        return mapa.toArray(new String[mapa.size()]);
    }

    public void setMapaArquivo(File mapaArquivo) {
        this.mapaArquivo = mapaArquivo;
    }

    public void setMapaLargura(int largura) {
        mapaLargura = largura;
    }

    public void setMapaAltura(int altura) {
        mapaAltura = altura;
    }

    public int getMapaEscolhido() {
        return mapaEscolhido;
    }

    public File getMapaArquivo() {
        return mapaArquivo;
    }

    public int getMapaLargura() {
        return mapaLargura;
    }

    public int getMapaAltura() {
        return mapaAltura;
    }
}
