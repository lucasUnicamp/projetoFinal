package main;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;

public class TransicaoFase extends JPanel{
    private float opacidade = 0f;
    private Timer timer;

    public TransicaoFase(String mensagem, Runnable quandoAcabar) {
        setOpaque(false); // Painel transparente
        setLayout(new GridBagLayout()); // Centralizar o texto
        JLabel label = new JLabel(mensagem);
        label.setFont(new Font("Comic Sans MS", Font.BOLD, 40)); //A CARGO DO LEITOR
        label.setForeground(Color.WHITE);
        add(label);

        // Animação com Timer
        timer = new Timer(50, e -> {
            opacidade += 0.05f;
            if (opacidade >= 1f) {
                timer.stop();
                quandoAcabar.run();
            }
            repaint();
        });
    }

    public void iniciar() {
        timer.start();
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

}
