package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Som {
    Clip clip;
    String sons[] = new String[5];
    public Som() {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        sons[0] = Paths.get("resources", "sons", "musica.wav").toString();
        sons[1] = Paths.get("resources", "sons", "comer.wav").toString();
    }

    public void carregarArquivo(int i) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(sons[i]));
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (LineUnavailableException e){
            System.err.println("Erro ao carregar som.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar som.");
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Erro ao carregar som.");
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
