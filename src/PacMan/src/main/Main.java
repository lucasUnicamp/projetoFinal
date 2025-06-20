package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.Menu;

public class Main {
    public static void main(String[] args) throws Exception {
        // Cria a janela do jogo
        JFrame janela = new JFrame();
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(true);
        janela.setTitle("Pac-Man");
        
        // 
        JPanel painelExterno = new JPanel(new GridBagLayout());
        painelExterno.setBackground(Color.BLACK);

        PainelJogo painelJogo = new PainelJogo();
        painelExterno.add(painelJogo, new GridBagConstraints());

        janela.setContentPane(painelExterno);
        janela.pack();

        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
        
        painelJogo.comecarThread();
    }
}
