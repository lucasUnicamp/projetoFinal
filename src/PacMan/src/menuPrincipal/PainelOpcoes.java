package menuPrincipal;

import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridLayout;

public class PainelOpcoes extends JPanel implements ActionListener, ChangeListener{
    private Clip clip;
    private JButton pausar;
    private JSlider slider;
    private boolean musicaPausada = false;
    private FloatControl controleVolume;

    public PainelOpcoes(Clip clip) {
        this.clip = clip;
        setLayout(new GridLayout(2,1));

        //pause da música
        pausar = new JButton("Pausar Música");
        this.add(pausar);
        pausar.addActionListener(this);

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            controleVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
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


    }


    @Override public void actionPerformed(ActionEvent e) {
        if (musicaPausada) {
            clip.start();
            this.pausar.setText("Pausar Música");
            
        } else {
            clip.stop();
            this.pausar.setText("Retomar Música");
        }

        musicaPausada = !musicaPausada;
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
