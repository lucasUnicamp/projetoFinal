package menuPrincipal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.PainelJogo;

public class MenuPausa extends JPanel implements ActionListener{
    JButton botaoRetomar;
    JButton botaoSalvar;
    JButton botaoSair;
    PainelJogo painelJogo;
    public MenuPausa(PainelJogo painelJogo) {
        super(new GridBagLayout());

        this.painelJogo = painelJogo;

        botaoRetomar = new JButton("Retomar");
        botaoSalvar = new JButton("Salvar");
        botaoSair = new JButton("Voltar Para o Menu");

        botaoRetomar.setPreferredSize(new Dimension(300, 50));
        botaoSalvar.setPreferredSize(new Dimension(300, 50));
        botaoSair.setPreferredSize(new Dimension(300, 50));
        botaoRetomar.addActionListener(this);
        botaoSalvar.addActionListener(this);
        botaoSair.addActionListener(this);


        setBackground(new Color(0, 0, 0, 230));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(50, 300, 50, 300);
        add(botaoRetomar, constraints);
        add(botaoSalvar, constraints);
        add(botaoSair, constraints);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoRetomar) {
            painelJogo.getPainelVidro().setVisible(false);
            painelJogo.setPausado(false);
        } else if(e.getSource() == botaoSalvar) {
            painelJogo.salvarJogo();
        } else if(e.getSource() == botaoSair) {
            painelJogo.voltarMenu();
        }
    }
}
