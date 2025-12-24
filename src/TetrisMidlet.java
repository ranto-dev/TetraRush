import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class TetrisMidlet extends MIDlet {

    private Display display;
    private MenuCanvas menu;

    public void startApp() {
        display = Display.getDisplay(this);
        if(menu == null) menu = new MenuCanvas(this);
        display.setCurrent(menu);
    }

    public void pauseApp() {}

    public void destroyApp(boolean unconditional) {}

    public void showGame() {
        display.setCurrent(new GameCanvasTetris(this));
    }

    public void showAbout() {
        display.setCurrent(new AboutCanvas(this));
    }

    public void showHelp() {
        display.setCurrent(new HelpCanvas(this));
    }

    public void backToMenu() {
        display.setCurrent(menu);
    }
}