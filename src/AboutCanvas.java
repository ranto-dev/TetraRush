import javax.microedition.lcdui.*;

public class AboutCanvas extends Canvas {

    private TetrisMidlet midlet;

    public AboutCanvas(TetrisMidlet m){ midlet=m; }

    protected void paint(Graphics g){
        g.setColor(15,15,30);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(0,255,120);
        g.drawString("ABOUT", getWidth()/2, 20, Graphics.HCENTER);

        g.setColor(255,255,255);
        g.drawString("Java ME Tetris Game", 5,60,0);
        g.drawString("Author: Student Project",5,85,0);
        g.drawString("Tech: MIDP 2.0 / CLDC 1.1",5,110,0);

        g.drawString("Press BACK", getWidth()/2, getHeight()-20, Graphics.HCENTER);
    }

    protected void keyPressed(int k){
        midlet.backToMenu();
    }
}
