import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class TetrisMidlet extends MIDlet {

    private Display display;
    private GameCanvasTetris game;

    public TetrisMidlet() {
        display = Display.getDisplay(this);
    }

    protected void startApp() {
        if(game == null) game = new GameCanvasTetris(this);
        display.setCurrent(game);
    }

    protected void pauseApp() {}

    protected void destroyApp(boolean unconditional) {}

    public void restartGame() {
        game = new GameCanvasTetris(this);
        display.setCurrent(game);
    }

    public void exit() {
        notifyDestroyed();
    }
}
