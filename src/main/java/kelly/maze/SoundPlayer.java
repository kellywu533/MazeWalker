package kelly;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *Sound player to play sounds according to occurrences of the maze walker
 */
public class SoundPlayer {
    private static final String AUDIO_DIR = "/System/Library/Sounds/";

    /**
     * Type of moves and their corresponding sounds
     */
    public enum Type {
        MOVEBACK("Pop.aiff")
        , HITWALL("Tink.aiff")
        , MOVE("Pop.aiff")
        , FINISH("Funk.aiff")
        ;

        byte[] soundData;
        Type(String fname) {
            try {
                Path path = Paths.get(AUDIO_DIR, fname);
                soundData = Files.readAllBytes(path);
            } catch (IOException e) {
                System.out.println("unable to load sound clip " + fname);
            }
        }

        /**
         *
         * @return
         */
        public InputStream soundDataInputStream() {
            if(soundData == null) {
                return null;
            }
            return new ByteArrayInputStream(soundData);
        }
    }

    /**
     *Plays the sound corresponding to each maze walker movement type
     * @param is
     */
    public static void playSound(InputStream is) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(is)) {
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener((event) -> {
                LineEvent.Type type = event.getType();
                if (type == LineEvent.Type.OPEN) {

//                        );
                } else if (type == LineEvent.Type.CLOSE) {

//                        );
                } else if (type == LineEvent.Type.START) {

//                        );
                } else if (type == LineEvent.Type.STOP) {

//                        );
                    clip.close();
                }
            });
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param t
     */
    public static void playSound(Type t) {
        if(t.soundDataInputStream() == null) {
            System.out.println("no sound " + t);
            return;
        }
        playSound(t.soundDataInputStream());
    }

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i<10; i++) {
            System.out.println(i);
            playSound(Type.MOVE);
            Thread.sleep(10);
        }
    }
}