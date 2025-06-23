package menuPrincipal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPausa extends JPanel{
    JButton botaoRetomar;
    JButton botaoSalvar;
    JButton botaoSair;
    public MenuPausa() {
        super(new GridBagLayout());

        botaoRetomar = new JButton("Retomar");
        botaoSalvar = new JButton("Salvar");
        botaoSair = new JButton("Sair");

        botaoRetomar.setPreferredSize(new Dimension(300, 50));
        botaoSalvar.setPreferredSize(new Dimension(300, 50));
        botaoSair.setPreferredSize(new Dimension(300, 50));

        setBackground(new Color(0, 0, 0, 230));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(50, 300, 50, 300);
        add(botaoRetomar, constraints);
        add(botaoSalvar, constraints);
        add(botaoSair, constraints);
    }
}
