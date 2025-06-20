package menuPrincipal;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MenuPrincipal extends JFrame{
    private CardLayout cardLayout;
    private JPanel cards;

    public MenuPrincipal() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Pac-Man");
        this.cardLayout =  new CardLayout();
        this.cards = new JPanel(cardLayout);

        //criação dos paineis
        PainelMenu menu = new PainelMenu(this);
        
        this.add(cards);
        this.cardLayout.show(cards, "painelMenu");
        this.pack();
        this.setVisible(true);
    }

    public CardLayout getCardLayout() {
        return this.cardLayout;
    }

    public JPanel getCards() {
        return this.cards;
    }
}
