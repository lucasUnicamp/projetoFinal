package menuPrincipal;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.LeitorTeclado;
import main.PainelJogo;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class MenuPrincipal extends JFrame{
    private CardLayout cardLayout;
    private JPanel cards;
    PainelOpcoes painelOpcoes;
    PainelJogo painelJogo;
    JPanel painelExternoJogo;
    PainelMenu painelMenu;
    Clip clip;
    LeitorTeclado leitor = new LeitorTeclado();

    public MenuPrincipal() {
        try {
            this.clip = AudioSystem.getClip();
        } catch(LineUnavailableException e) {
            System.out.println("Erro ao carregar música:");
            e.printStackTrace();
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Pac-Man");
        this.cardLayout =  new CardLayout();
        this.cards = new JPanel(cardLayout);
        cards.addKeyListener(leitor);
        cards.setFocusable(true);

        painelExternoJogo = new JPanel(new GridBagLayout());
        painelExternoJogo.setBackground(Color.BLACK);

        painelJogo = new PainelJogo(leitor);
        painelExternoJogo.add(painelJogo, new GridBagConstraints());

        painelMenu = new PainelMenu(clip, this);
        
        painelOpcoes = new PainelOpcoes(clip, this);

        getCards().add(painelMenu, "painelMenu");
        getCards().add(painelOpcoes, "painelOpcoes");
        getCards().add(painelExternoJogo, "painelJogo");

        this.add(cards);
        this.cardLayout.show(cards, "painelMenu");
        this.pack();
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public CardLayout getCardLayout() {
        return this.cardLayout;
    }

    public JPanel getCards() {
        return this.cards;
    }
}
