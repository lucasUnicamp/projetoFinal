package menuPrincipal;

import javax.swing.JPanel;

import main.Som;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.sound.sampled.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class PainelMenu extends JPanel implements ActionListener{
    //botões
    private JButton novoJogo;
    private JButton continuar;
    private JButton opcoes;
    private JButton sair;

    //frame que contém os paineis
    private MenuPrincipal frame;
    private Som som;

    public PainelMenu (Som som, MenuPrincipal frame) {
        setPreferredSize(new Dimension (500, 500));
        this.som = som;
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
            frame.getCardLayout().show(this.frame.getCards(), "painelJogo");
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.painelExternoJogo.painelJogo.novoJogo(0);
            frame.painelExternoJogo.painelJogo.comecarThread();

        } else if (e.getSource() == continuar) {
            frame.getCardLayout().show(this.frame.getCards(), "painelJogo");
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.painelExternoJogo.painelJogo.carregarJogo();
            frame.painelExternoJogo.painelJogo.comecarThread();

        } else if (e.getSource() == opcoes){
            this.frame.getCardLayout().show(this.frame.getCards(), "painelOpcoes");

        } else {
            frame.dispose();
        }
    }

    private void tocarMusica() {

        File arquivo = new File(Paths.get("resources", "sons", "musica.wav").toString());

        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
            return;
        }

        som.tocarMusica(0);
    

    }
    


}