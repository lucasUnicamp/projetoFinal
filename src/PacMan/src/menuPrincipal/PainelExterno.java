package menuPrincipal;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.LeitorTeclado;
import main.PainelJogo;
import main.Som;

public class PainelExterno extends JPanel {
    JLabel labelPontos, labelCentro, labelCanto;
    PainelJogo painelJogo;
    private Font fonte;

    public PainelExterno(LeitorTeclado leitor, JPanel cards, JComponent painelVidro, Som som) {
        super(new GridBagLayout());
        labelPontos = new JLabel("Pontuação: ");
        labelCanto = new JLabel("3...");
        fonte = carregarFonte();

        labelPontos.setFont(fonte);
        labelCanto.setFont(fonte);
        labelPontos.setForeground(Color.WHITE);
        labelCanto.setForeground(Color.WHITE);

        add(labelPontos, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
        add(labelCanto, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
        
        GridBagConstraints constraints = new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        constraints.gridy = 1;
        painelJogo = new PainelJogo(leitor, this, cards, painelVidro, som);
        setPreferredSize(painelJogo.getPreferredSize());
        add(painelJogo, constraints);
    }

    public void setTextoLabelPontos(String texto) {
        labelPontos.setText(texto);
    }

    public void setTextoLabelCanto(String texto) {
        labelCanto.setText(texto);
    }

    public JLabel getLabelPontos() {
        return labelPontos;
    }

    public JLabel getLabelCanto() {
        return labelCanto;
    }

    public Font carregarFonte() {
        Font fonte = null;

        try {
            // '.deriveFont' é o tamanho da fonte
            fonte = Font.createFont(Font.TRUETYPE_FONT, new File(Paths.get("resources", "fontes", "silkscreenBold.ttf").toString())).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // Registra a fonte
            ge.registerFont(fonte);
        } catch (IOException erro) {
            System.err.println("!!! ERRO AO ABRIR ARQUIVO DA FONTE !!!");
        } catch(FontFormatException erro) {
            System.err.println("!!! ERRO NA FONTE !!!");
        }

        return fonte;
    }
}
