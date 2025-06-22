package menuPrincipal;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.LeitorTeclado;
import main.PainelJogo;

public class PainelExterno extends JPanel{
    JLabel labelPontos;
    PainelJogo painelJogo;
    public PainelExterno(LeitorTeclado leitor) {
        super(new GridBagLayout());
        labelPontos = new JLabel("Pontuação:");
        labelPontos.setFont(new Font("SansSerif", Font.BOLD, 24));
        labelPontos.setForeground(Color.WHITE);
        add(labelPontos, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
        
        GridBagConstraints constraints = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        constraints.gridy = 1;
        painelJogo = new PainelJogo(leitor, this);
        setPreferredSize(painelJogo.getPreferredSize());
        add(painelJogo, constraints);
    }

    public void setTextoLabelPontos(String texto) {
        labelPontos.setText(texto);
    }

    public JLabel getLabelPontos() {
        return labelPontos;
    }
}
