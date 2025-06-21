package main;

import excecoes.ArquivoCorrompidoException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.nio.file.Paths;

public class TratadorMapa {
    private int mapaEscolhido;
    private int mapaLargura;
    private int mapaAltura;
    private final int maxLargura;
    private final int maxAltura;
    private File mapaArquivo;

    public TratadorMapa(int mapaEscolhido) {
        maxLargura = 40;
        maxAltura = 30;
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
            setMapaArquivo(mapaArquivo);
            checarMapa();
        }
        // Mesmo se o 'mapa0' já existir, cria outro por precaução
        else {
            System.out.println("Nenhum mapa encontrado. Configuração genérica será usada.");
            criarGenerico();
        }
    }

    // Cria o mapa genérico para que o jogo não fique sem nenhum
    public void criarGenerico() {
        System.out.println("Mapa genérico sendo criado...");
        try {
            Formatter arquivoLog  = new Formatter(new FileWriter(Paths.get("resources", "mapas", "mapa0").toString(), false));
            arquivoLog.format("############################\n" +
                              "#............##............#\n" + 
                              "#.####.#####.##.#####.####.#\n" + 
                              "#.####.#####.##.#####.####.#\n" + 
                              "#..........................#\n" + 
                              "#.####.##.########.##.####.#\n" + 
                              "#.####.##.########.##.####.#\n" + 
                              "#......##....##....##......#\n" + 
                              "######.#####.##.#####.######\n" + 
                              "######.#####.##.#####.######\n" + 
                              "######.##..........##.######\n" + 
                              "######.##.###..###.##.######\n" + 
                              "######.##.#......#.##.######\n" + 
                              "<.........#......#.........#\n" + 
                              "######.##.#......#.##.######\n" + 
                              "######.##.########.##.######\n" + 
                              "######.##..........##.######\n" + 
                              "######.##.########.##.######\n" + 
                              "######.##.########.##.######\n" +
                              "#............##............#\n" + 
                              "#.####.#####.##.#####.####.#\n" + 
                              "#.####.#####.##.#####.####.#\n" + 
                              "#...##................##...#\n" + 
                              "###.##.##.########.##.##.###\n" + 
                              "###.##.##.########.##.##.###\n" + 
                              "#......##....##....##......#\n" + 
                              "#.##########.##.##########.#\n" + 
                              "#.##########.##.##########.#\n" + 
                              "#..........................#\n" + 
                              "############################");
            arquivoLog.flush();
            arquivoLog.close();
            setMapaArquivo(new File(Paths.get("resources", "mapas", "mapa0").toString()));
        } catch (IOException erro) {
            System.err.println("!!! ERRO AO CRIAR MAPA GENÉRICO !!!");
        }
    }

    /**
     * Percorre o mapa para checar se é válido para o jogo. Checa se não há caracteres inválidos ou tamanhos inválidos.
     * Caso algo esteja errado, gera o mapa genérico no lugar
     */
    public void checarMapa() {
        int altura = 0;
        int largura, larguraUltima = 0;
        boolean comecouTunelHorizontal;
        boolean[] comecouTuneis = new boolean[maxLargura];

        System.out.println("Checando mapa...");
        try (Scanner scan = new Scanner(new FileReader(getMapaArquivo()))) {
            String linha;

            while (scan.hasNextLine()) {
                linha = scan.nextLine();
                largura = linha.length();
                comecouTunelHorizontal = false;

                if (altura != 0) {
                    // Caso o mapa não seja em formato retangular
                    if (larguraUltima != largura)
                        throw new ArquivoCorrompidoException(String.format("LINHA %d DE TAMANHO DIFERENTE DA ANTERIOR", altura));
                }

                // Caso alguma linha seja maior que o máximo permitido
                if (largura > getMaxLargura())
                    throw new ArquivoCorrompidoException(String.format("HÁ UMA LINHA MAIOR QUE O PERMITIDO", altura));
                // Caso alguma coluna seja maior que o máximo permitido
                if (altura > getMaxAltura())
                    throw new ArquivoCorrompidoException(String.format("HÁ UMA COLUNA MAIOR QUE O PERMITIDO"));                

                for (int i = 0; i < largura; i++) {
                    // Caso algum caractere inválido esteja no mapa
                    if (linha.charAt(i) != '#' && linha.charAt(i) != '.' && linha.charAt(i) != '<')
                        throw new ArquivoCorrompidoException(String.format("CARACTERE NÃO RECONHECIDO EM [%d, %d]", i, altura));
                    // Caso algum tunel seja posto no interior do mapa ao invés de só nas bordas
                    if (((altura != 0 && scan.hasNextLine()) && (i != 0 && i != largura - 1)) && linha.charAt(i) == '<')
                        throw new ArquivoCorrompidoException(String.format("TUNEL COLOCADO NO INTERIOR DO MAPA EM [%d, %d]", i, altura));

                    // Caso em que apenas uma parte do túnel tenha sido posta horizontalmente
                    if (i == 0 && linha.charAt(i) == '<')
                        comecouTunelHorizontal = true;
                    if ((i == largura - 1 && comecouTunelHorizontal && linha.charAt(i) != '<') || (i == largura - 1 && !comecouTunelHorizontal && linha.charAt(i) == '<'))
                        throw new ArquivoCorrompidoException(String.format("TUNEL COMEÇADO MAS NÃO ACABADO NA LINHA EM [X, %d]", altura));

                    // Caso em que apenas uma parte do túnel tenha sido posta verticalmente
                    if (altura == 0 && linha.charAt(i) == '<')
                        comecouTuneis[i] = true;
                    if ((!scan.hasNextLine() && comecouTuneis[i] && linha.charAt(i) != '<') || (!scan.hasNextLine() && !comecouTuneis[i] && linha.charAt(i) == '<'))
                        throw new ArquivoCorrompidoException(String.format("TUNEL COMEÇADO MAS NÃO ACABADO NA COLUNA EM [%d, X]", i));
                }
                larguraUltima = largura;
                altura++;
            }
        } catch (IOException erro) {
            System.err.println("!!! ARQUIVO NÃO FOI ENCONTRADO PARA SER ATRIBUIDO !!!");
        } catch (ArquivoCorrompidoException erro) {
            System.err.println(erro.getMessage());
            criarGenerico();
        }
    }

    /**
     * Converte o arquivo de texto em um array de string para ser usado no jogo. Durante a conversão, 
     * também atribui ao objeto suas medições 
     * @return String[] do mapa baseada no arquivo de mapa
     */
    public String[] atribuirMapa() {
        int contador = 0;
        ArrayList<String> mapa = new ArrayList<String>();

        try (Scanner scan = new Scanner(new FileReader(getMapaArquivo()))) {
            String linha;

            while (scan.hasNextLine()) {
                linha = scan.nextLine();
                // Pega o comprimento da primeira linha para usar como largura
                if (contador == 0)
                    setMapaLargura(linha.length());
                mapa.add(linha);  
                contador++;
            }

        } catch (IOException erro) {
            System.err.println("!!! ARQUIVO NÃO FOI ENCONTRADO PARA SER ATRIBUIDO !!!");
        }

        // Usa o número total de linhas para usar como altura
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

    public int getMaxLargura() {
        return maxLargura;
    }

    public int getMaxAltura() {
        return maxAltura;
    }
}
