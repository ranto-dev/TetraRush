/**
 * Midlet principal: TetrisMidlet
 */
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class TetrisMidlet extends MIDlet implements CommandListener {

    private Display display;
    private List menuScreen;
    private GameCanvasTetris gameCanvas;
    private Command cmdBack = new Command("Back", Command.BACK, 1);
    private Command cmdExit = new Command("Exit", Command.EXIT, 2);

    public void startApp() {
        display = Display.getDisplay(this);

        menuScreen = new List("TETRIS GAME", Choice.IMPLICIT);
        menuScreen.append("1. Commencer une partie", null);
        menuScreen.append("2. A propos du jeu", null);
        menuScreen.append("3. Aide", null);

        menuScreen.addCommand(cmdExit);
        menuScreen.setCommandListener(this);

        display.setCurrent(menuScreen);
    }

    public void pauseApp() {}

    public void destroyApp(boolean u) {}

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            notifyDestroyed();
            return;
        }

        if (d == menuScreen) {
            int idx = menuScreen.getSelectedIndex();
            switch (idx) {
                case 0: 
                    // Commencer partie
                    restartGame();
                    break;
                case 1: 
                    // section "A propos"
                    Form about = new Form("A propos");
                    about.append(
                        "Mini Tetris Game\n" +
                        "Java ME MIDP 1.0\n" +
                        "Student Project\n"
                    );
                    about.addCommand(cmdBack);
                    about.setCommandListener(this);
                    display.setCurrent(about);
                    break;
                case 2: 
                    // section "Aide"
                    Form help = new Form("Aide");
                    help.append(
                        "Comment jouer ?\n\n" +
                        "- LEFT / RIGHT: Déplacer\n" +
                        "- FIRE: Tourner\n" +
                        "- DOWN: Descendre\n" +
                        "- * Pause\n" +
                        "- # Menu\n"
                    );
                    help.addCommand(cmdBack);
                    help.setCommandListener(this);
                    display.setCurrent(help);
                    break;
            }
        } else if (c == cmdBack) {
            display.setCurrent(menuScreen);
        }
    }

    public void restartGame() {
        gameCanvas = new GameCanvasTetris(this);
        display.setCurrent(gameCanvas);
    }

    public void backToMenu() {
        display.setCurrent(menuScreen);
    }
}
