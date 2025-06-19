package menuPrincipal;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.sound.sampled.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class PainelMenu extends JPanel implements ActionListener{
    private JButton novoJogo;
    private JButton continuar;
    private JButton opcoes;
    private JButton sair;
    private JFrame frame;
    private Clip clip;

    public PainelMenu (JFrame frame) {
        this.frame = frame;
        this.tocarMusica("");
        this.setLayout(new GridLayout(4, 1));
        
        novoJogo = new JButton("Novo Jogo");
        continuar = new JButton("Continuar");
        opcoes = new JButton("Opções");
        sair = new JButton("Sair");

        this.add(novoJogo);
        this.add(continuar);
        this.add(opcoes);
        this.add(sair);

    }

    @Override public void actionPerformed (ActionEvent e) {
        if (e.getSource() == novoJogo) {
            //ação correspondente

        } else if (e.getSource() == continuar) {
            //ação correspondente

        } else if (e.getSource() == opcoes){
            //ação correspondente

        } else {
            frame.dispose();
        }
    }

    private void tocarMusica(String caminhoAudio) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(caminhoAudio));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {}
    }
}