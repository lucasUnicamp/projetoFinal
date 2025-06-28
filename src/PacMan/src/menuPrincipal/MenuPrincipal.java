package menuPrincipal;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.LeitorTeclado;
import main.PainelJogo;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuPrincipal extends JFrame{
    private CardLayout cardLayout;
    private JPanel cards;
    PainelOpcoes painelOpcoes;
    PainelJogo painelJogo;
    PainelExterno painelExternoJogo;
    PainelMenu painelMenu;
    Clip clip;
    LeitorTeclado leitor = new LeitorTeclado(this);
    JComponent painelVidro;

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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                painelExternoJogo.painelJogo.salvarJogo();
                if(painelExternoJogo.painelJogo.getThread() != null) {
                    painelExternoJogo.painelJogo.getThread().interrupt();
                }
                dispose();
                System.exit(0);
            }
        });

        this.painelVidro = (JComponent) getGlassPane();
        this.cardLayout =  new CardLayout();
        this.cards = new JPanel(cardLayout);
        cards.addKeyListener(leitor);
        cards.setFocusable(true);

        painelExternoJogo = new PainelExterno(leitor, getCards(), painelVidro);
        painelExternoJogo.setBackground(Color.BLACK);

        painelMenu = new PainelMenu(clip, this);
        
        painelOpcoes = new PainelOpcoes(clip, this);

        getCards().add(painelMenu, "painelMenu");
        getCards().add(painelOpcoes, "painelOpcoes");
        getCards().add(painelExternoJogo, "painelJogo");

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "PAUSAR");
        getRootPane().getActionMap().put("PAUSAR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(painelExternoJogo.isVisible()) {
                    painelVidro.removeAll();
                    painelVidro.revalidate();
                    painelVidro.repaint();;
                    painelVidro.add(new MenuPausa(painelExternoJogo.painelJogo));
                    if(!painelExternoJogo.painelJogo.estaPausado()) {
                        painelExternoJogo.painelJogo.setPausado(true);
                        ((JComponent) getGlassPane()).setVisible(true);
                    } else {
                        painelExternoJogo.painelJogo.setPausado(false);
                        ((JComponent) getGlassPane()).setVisible(false);
                    }
                }
            }
            
        });
        this.add(cards);
        this.cardLayout.show(cards, "painelMenu");
        this.pack();
        this.setMinimumSize(getSize());
        this.setVisible(true);
        painelVidro.setLayout(new GridBagLayout());
        painelVidro.setOpaque(false);

    }

    public CardLayout getCardLayout() {
        return this.cardLayout;
    }

    public JPanel getCards() {
        return this.cards;
    }
}
