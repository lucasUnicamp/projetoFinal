package menuPrincipal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.Som;

public class PainelMenu extends JPanel implements ActionListener{
    //botões
    private JLabel labelTitulo;
    private JButton novoJogo;
    private JButton continuar;
    private JButton opcoes;
    private JButton sair;
    Font fonte;

    //frame que contém os paineis
    private MenuPrincipal frame;
    private Som som;
    ImageIcon titulo;

    public PainelMenu (Som som, MenuPrincipal frame) {
        setPreferredSize(new Dimension (800, 500));
        setBackground(Color.BLACK);
        fonte = carregarFonte();
        this.som = som;
        this.frame = frame;

        carregarImagem();
        this.tocarMusica();
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.fill = GridBagConstraints.BOTH;
        labelTitulo = new JLabel(titulo);
        novoJogo = new JButton("Novo Jogo");
        continuar = new JButton("Continuar");
        opcoes = new JButton("Opções");
        sair = new JButton("Sair");

        novoJogo.setFont(fonte);
        continuar.setFont(fonte);
        opcoes.setFont(fonte);
        sair.setFont(fonte);

        this.add(labelTitulo);
        this.add(novoJogo, constraints);
        this.add(continuar, constraints);
        this.add(opcoes, constraints);
        this.add(sair, constraints);

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

    private void carregarImagem() {
        titulo = new ImageIcon(Paths.get("resources", "imagens", "logoGrande.png").toString());
    } 

    public Font carregarFonte() {

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