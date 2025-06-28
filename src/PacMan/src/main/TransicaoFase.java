package main;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.AlphaComposite;

public class TransicaoFase extends JPanel {
    private float opacidade;
    private Timer timer;
    private Timer timer2;
    private Font fonte;

    public TransicaoFase(String mensagem, Runnable quandoAcabar) {
        setOpaque(false); // Painel transparente
        setLayout(new GridBagLayout()); // Centralizar o texto
        JLabel label = new JLabel(mensagem);
        fonte = carregarFonte();
        label.setFont(fonte); //A CARGO DO LEITOR
        label.setForeground(Color.WHITE);
        add(label);

        // Animação com Timer
        timer = new Timer(100, e -> {
            opacidade += 0.05f;
            System.out.println(opacidade);
            if (opacidade >= 1f) {
                timer.stop();
                quandoAcabar.run();
            }
            repaint();
        });
        timer2 = new Timer(100, e -> {
            opacidade -= 0.02f;

            if (opacidade <= 0.8f) {
                label.setText("");
                opacidade -= 0.1f;
            }
            if (opacidade <= 0f) {
                timer2.stop();
                quandoAcabar.run();
            }
            repaint();
        });
    }

    public void iniciar() {
        timer.start();
    }

    public void finalizar() {
        timer2.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidade));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
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

    public void setOpacidade(float opa) {
        opacidade = opa;
    }
}
