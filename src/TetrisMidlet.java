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

        menuScreen = new List("Tetra Rush", Choice.IMPLICIT);
        menuScreen.append("1. Commencer une nouvelle partie", null);
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
                        " ~ Tetra Rush ~ \n\n" +
                        "         Tout le monde connaît le célèbre jeu \"Tetris\", l\'un des jeux les plus iconiques de l\'histoire du jeu vidéo, largement présent sur les appareils MicroEdition." +
                        " Pourquoi développer cette application ? " +
                        " D\'une part pour pratiquer et renforcer mes compétences en Java, en particulier dans le développement MicroEdition. D\'autre part, pour le plaisir, et par passion pour les jeux rétro qui ont marqué l\'histoire." +
                        " --------------------- \n" +
                        "Autheur: Ranto andrianandraina\n" +
                        "         http://ranto-dev.vercel.app \n" +
                        "         http://github.com/ranto-dev \n"
                    );
                    about.addCommand(cmdBack);
                    about.setCommandListener(this);
                    display.setCurrent(about);
                    break;
                case 2: 
                    // section "Aide"
                    Form help = new Form("Aide");
                    help.append(
                        " ~ AIDE - Commandes du jeu ~ \n\n" +
                        "Déplacement de la pièce :\n" +
                        "    - Gauche  : Touche Directionnelle Gauche\n" +
                        "    - Droite  : Touche Directionnelle Droite \n" +
                        "Rotation :\n" +
                        "   - Tourner la pièce : Touche Directionnelle Haut\n" +
                        "Vitesse de descente :\n" +
                        "    - Accélérer la descente : Touche Directionnelle Bas\n" +
                        "Gestion du jeu :\n" +
                        "    - Pause / Reprendre : Touche 0\n" +
                        "    - Quitter la partie : Touche #\n" +
                        "    - Valider / OK / Jouer : Touche centrale ou FIRE\n" +

                        "Objectif du jeu :\n" +
                        "    Alignez une ligne complète de blocs pour la faire disparaître" +
                        "et gagner des points. Le jeu se termine lorsque les pièces" +
                        "atteignent le haut de la grille.\n" +

                        "Astuce : Anticipez la prochaine pièce pour mieux planifier vos mouvements.\n" +
                        "----- Bonne chance et amusez-vous ! -----"
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
