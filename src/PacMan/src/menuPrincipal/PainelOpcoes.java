package menuPrincipal;

import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Som;

import java.awt.GridLayout;

public class PainelOpcoes extends JPanel implements ActionListener, ChangeListener{
    private Som som;
    private JButton pausar;
    private JButton voltar;
    private JSlider slider;
    private boolean musicaPausada = false;
    private FloatControl controleVolume;
    private MenuPrincipal frame;


    public PainelOpcoes(Som som, MenuPrincipal frame) {
        this.frame = frame;
        this.som = som;
        
        setLayout(new GridLayout(2,1));

        //pause da música
        pausar = new JButton("Pausar Música");
        this.add(pausar);
        pausar.addActionListener(this);
        
        //controlador de volume
        if (som.getClip().isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            controleVolume = (FloatControl) som.getClip().getControl(FloatControl.Type.MASTER_GAIN);
            slider = new JSlider(0, 100, 70); // 0 a 100, começa em 70
            slider.setMajorTickSpacing(10);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(this);
            this.add(slider);
        } else {
            slider = new JSlider();
            slider.setEnabled(false); // Desabilita se não for suportado
        }

        //botão de retornar
        voltar = new JButton("Voltar");
        voltar.addActionListener(this);
        this.add(voltar);
    }


    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.pausar) {
            if (musicaPausada) {
                som.tocarMusica(0);
                
            } else {
                som.parar();
            }
    
            musicaPausada = !musicaPausada;

        } else {
            //clicou na opção de voltar
            this.frame.getCardLayout().show(this.frame.getCards(), "painelMenu");
        }
    }

    @Override public void stateChanged(ChangeEvent e) {
        int valorSlider = slider.getValue(); // 0 a 100
        float volumeMin = controleVolume.getMinimum(); // ex: -80.0
        float volumeMax = controleVolume.getMaximum(); // ex: 6.0

        // Converte valor do slider (0–100) para o intervalo de volume real
        float volumeReal = volumeMin + (volumeMax - volumeMin) * (valorSlider / 100f);
        controleVolume.setValue(volumeReal);
    }
}
