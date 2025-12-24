import javax.microedition.media.*;

public class SoundManager {

    private static Player musicPlayer;

    public static void playMusic(String res){
        try{
            if(musicPlayer != null) musicPlayer.close();
            musicPlayer = Manager.createPlayer(
                SoundManager.class.getResourceAsStream(res),
                "audio/x-wav");
            musicPlayer.setLoopCount(-1); // loop forever
            musicPlayer.start();
        } catch(Exception e){}
    }

    public static void playSfx(String res){
        try{
            Player p = Manager.createPlayer(
                SoundManager.class.getResourceAsStream(res),
                "audio/x-wav");
            p.start();
        } catch(Exception e){}
    }

    public static void stopMusic(){
        try{
            if(musicPlayer != null) musicPlayer.stop();
        } catch(Exception e){}
    }
}
