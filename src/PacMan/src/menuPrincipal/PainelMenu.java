package menuPrincipal;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PainelMenu extends JPanel {
    JPanel painel;


    public PainelMenu () {
        this.painel = new JPanel();
        this.painel.setLayout(new GridLayout(4, 1));
        
        JButton novoJogo = new JButton("Novo Jogo");
        JButton continuar = new JButton("Continuar");
        JButton opcoes = new JButton("Opções");
        JButton sair = new JButton("Sair");

        this.painel.add(novoJogo);
        this.painel.add(continuar);
        this.painel.add(opcoes);
        this.painel.add(sair);

        

    }
}