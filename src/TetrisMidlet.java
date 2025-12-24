import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class TetrisMidlet extends MIDlet implements CommandListener {

    private Display display;
    private Form menuScreen;
    private GameCanvasTetris gameCanvas;

    private Command cmdPlay = new Command("Play", Command.OK, 1);
    private Command cmdHelp = new Command("Help", Command.SCREEN, 2);
    private Command cmdAbout = new Command("About", Command.SCREEN, 3);
    private Command cmdExit = new Command("Exit", Command.EXIT, 4);
    private Command cmdBack = new Command("Back", Command.BACK, 1);

    public void startApp() {
        display = Display.getDisplay(this);

        menuScreen = new Form("TETRIS MIDP1.0");
        menuScreen.append("🎮 Mini TETRIS\n\nCLDC 1.1 / MIDP 1.0\n");
        menuScreen.addCommand(cmdPlay);
        menuScreen.addCommand(cmdHelp);
        menuScreen.addCommand(cmdAbout);
        menuScreen.addCommand(cmdExit);
        menuScreen.setCommandListener(this);

        display.setCurrent(menuScreen);
    }

    public void pauseApp() {}

    public void destroyApp(boolean u) {}

    public void commandAction(Command c, Displayable d) {

        if(c == cmdPlay) {
            restartGame();
        }
        else if(c == cmdHelp) {
            Form help = new Form("Help");
            help.append(
                "HOW TO PLAY\n\n" +
                "- LEFT / RIGHT: Move\n" +
                "- FIRE: Rotate\n" +
                "- DOWN: Drop\n" +
                "- * Pause\n" +
                "- # Menu\n"
            );
            help.addCommand(cmdBack);
            help.setCommandListener(this);
            display.setCurrent(help);
        }
        else if(c == cmdAbout) {
            Form about = new Form("About");
            about.append(
                "Mini Tetris Game\n" +
                "Java ME MIDP 1.0\n" +
                "Student Project\n"
            );
            about.addCommand(cmdBack);
            about.setCommandListener(this);
            display.setCurrent(about);
        }
        else if(c == cmdBack) {
            display.setCurrent(menuScreen);
        }
        else if(c == cmdExit) {
            exit();
        }
    }

    public void restartGame() {
        gameCanvas = new GameCanvasTetris(this);
        display.setCurrent(gameCanvas);
    }

    public void backToMenu() {
        display.setCurrent(menuScreen);
    }

    public void exit() {
        notifyDestroyed();
    }
}
