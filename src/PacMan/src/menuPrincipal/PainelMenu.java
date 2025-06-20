package menuPrincipal;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.sound.sampled.*;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import main.*;

public class PainelMenu extends JPanel implements ActionListener{
    //botões
    private JButton novoJogo;
    private JButton continuar;
    private JButton opcoes;
    private JButton sair;

    //frame que contém os paineis
    private MenuPrincipal frame;
    private Clip clip;

    public PainelMenu (Clip clip, MenuPrincipal frame) {
        this.clip = clip;
        this.frame = frame;

        this.tocarMusica();
        this.setLayout(new GridLayout(4, 1));
        
        novoJogo = new JButton("Novo Jogo");
        continuar = new JButton("Continuar");
        opcoes = new JButton("Opções");
        sair = new JButton("Sair");

        this.add(novoJogo);
        this.add(continuar);
        this.add(opcoes);
        this.add(sair);

        novoJogo.addActionListener(this);
        continuar.addActionListener(this);
        opcoes.addActionListener(this);
        sair.addActionListener(this);

    }

    @Override public void actionPerformed (ActionEvent e) {
        if (e.getSource() == novoJogo) {
            this.frame.getCardLayout().show(this.frame.getCards(), "painelJogo");
            this.frame.requestFocusInWindow();
            this.frame.painelJogo.comecarThread();

        } else if (e.getSource() == continuar) {
            //ação correspondente

        } else if (e.getSource() == opcoes){
            this.frame.getCardLayout().show(this.frame.getCards(), "painelOpcoes");

        } else {
            frame.dispose();
        }
    }

    private void tocarMusica() {
        try {
            File arquivo = new File(Paths.get("resources", "sons", "musica.wav").toString());
    
            if (!arquivo.exists()) {
                System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
                return;
            }
    
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(arquivo);
            AudioFormat baseFormat = originalStream.getFormat();
    
            System.out.println("Formato original: " + baseFormat);
    
            // Converte para um formato compatível com Clip
            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false // little-endian
            );
    
            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, originalStream);
            clip.open(decodedStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    
            System.out.println("Música carregada e tocando!");
    
        } catch (LineUnavailableException e) {
            System.out.println("Erro ao carregar música:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro ao carregar música:");
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Erro ao carregar música:");
            e.printStackTrace();
        }

    }
    


}