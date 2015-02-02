package tools;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MakeSound {

    private final static int BUFFER_SIZE = 128000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    public  void playSound(){

    	
    	InputStream path = getClass().getResourceAsStream("Nanarland.wav");

    	
    	try
    	{
    	InputStream bufferedIn = new BufferedInputStream(path);
    	AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
    	Clip clip2 = AudioSystem.getClip();
    	clip2.open(audioStream);
    	clip2.start();
    	
    	}catch(Exception fail)
    	{
    	System.out.println(fail);
    	}
    } 
}
