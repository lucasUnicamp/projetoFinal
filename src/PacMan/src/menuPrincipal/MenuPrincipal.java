package menuPrincipal;

import javax.swing.JFrame;

public class MenuPrincipal extends JFrame {
    
    public MenuPrincipal() {
        this.setTitle("Menu Principal");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PainelMenu painel = new PainelMenu(this);
        this.add(painel);

        setLocationRelativeTo(null); //Centralização
        this.setVisible(true);

    }
}

