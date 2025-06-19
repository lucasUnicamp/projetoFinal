package menuPrincipal;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.sound.sampled.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
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
            //ação correspondente

        } else if (e.getSource() == continuar) {
            //ação correspondente

        } else if (e.getSource() == opcoes){
            JFrame janelaOpcoes = new JFrame("Opções");
            janelaOpcoes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            janelaOpcoes.setSize(300, 200);
            janelaOpcoes.setLocationRelativeTo(frame); // centraliza em relação ao menu
            janelaOpcoes.setContentPane(new PainelOpcoes(this.clip));
            janelaOpcoes.setVisible(true);

        } else {
            frame.dispose();
        }
    }

    private void tocarMusica() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(Paths.get("resources", "sons", "Pac-Man-Theme-(REMIX).wav").toString()));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {}
    }
}