package interfaces;
import java.awt.Graphics2D;

public interface Elemento{
    public void desenhar(Graphics2D g);
    public boolean ehColidivel();
    public char getRepresentacao();
}
