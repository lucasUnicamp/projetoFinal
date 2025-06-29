package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Som {
    private Clip clip;
    File[] arquivos = new File[5];
    public Som() {
        arquivos[0] = new File(Paths.get("resources", "sons", "musica.wav").toString());
        arquivos[1] = new File(Paths.get("resources", "sons", "comer.wav").toString());

    }

    public void carregarArquivo(int i) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(arquivos[i]);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void tocarEfeito(int i) {
        carregarArquivo(i);
        clip.start();
    }
    
    public void tocarMusica(int i) {
        carregarArquivo(i);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void parar() {
        clip.stop();
    }

    public Clip getClip() {
        return clip;
    }
}
