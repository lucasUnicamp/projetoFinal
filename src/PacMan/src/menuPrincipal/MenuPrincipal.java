package menuPrincipal;

import javax.sound.midi.SysexMessage;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.LeitorTeclado;
import main.PainelJogo;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.PopupMenu;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal extends JFrame{
    private CardLayout cardLayout;
    private JPanel cards;
    PainelOpcoes painelOpcoes;
    PainelJogo painelJogo;
    PainelExterno painelExternoJogo;
    PainelMenu painelMenu;
    Clip clip;
    LeitorTeclado leitor = new LeitorTeclado(this);

    public MenuPrincipal() {
        try {
            this.clip = AudioSystem.getClip();
        } catch(LineUnavailableException e) {
            System.out.println("Erro ao carregar música:");
            e.printStackTrace();
        }

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Pac-Man");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                painelExternoJogo.painelJogo.salvarJogo();
                dispose();
                System.exit(0);
            }
        });

        this.cardLayout =  new CardLayout();
        this.cards = new JPanel(cardLayout);
        cards.addKeyListener(leitor);
        cards.setFocusable(true);

        painelExternoJogo = new PainelExterno(leitor);
        painelExternoJogo.setBackground(Color.BLACK);

        MenuPausa menuPausa = new MenuPausa();

        painelMenu = new PainelMenu(clip, this);
        
        painelOpcoes = new PainelOpcoes(clip, this);

        getCards().add(painelMenu, "painelMenu");
        getCards().add(painelOpcoes, "painelOpcoes");
        getCards().add(painelExternoJogo, "painelJogo");

        this.add(cards);
        this.cardLayout.show(cards, "painelMenu");
        this.pack();
        this.setMinimumSize(getSize());
        this.setVisible(true);
        ((JComponent) getGlassPane()).setLayout(new GridBagLayout());
        ((JComponent) getGlassPane()).setOpaque(false);
        ((JComponent) getGlassPane()).add(menuPausa);

    }

    public CardLayout getCardLayout() {
        return this.cardLayout;
    }

    public JPanel getCards() {
        return this.cards;
    }
}
