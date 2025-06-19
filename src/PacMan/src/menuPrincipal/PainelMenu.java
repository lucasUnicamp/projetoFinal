package menuPrincipal;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PainelMenu extends JPanel implements ActionListener{
    JButton novoJogo;
    JButton continuar;
    JButton opcoes;
    JButton sair;
    JFrame frame;

    public PainelMenu (JFrame frame) {
        this.frame = frame;
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
}