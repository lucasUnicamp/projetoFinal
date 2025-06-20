package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.nio.file.Paths;

public class TratadorArquivo {
    public TratadorArquivo() {

    }

    public void checarMapa() {
        if (new File(Paths.get("resources", "mapas", "mapa1.txt").toString()).isFile()) {
            System.out.println("Mapa 1 carregado com sucesso.");
        }
        else {
            System.out.println("Nenhum mapa encontrado. Configuração genérica será usada.");
            criarGenerico();
        }
    }

    public void criarGenerico() {
        try {
            Formatter arquivoLog  = new Formatter(new FileWriter(Paths.get("resources", "mapas", "mapa1").toString(), false));
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
}
