import javax.microedition.lcdui.*;

public class HelpCanvas extends Canvas {

    private TetrisMidlet midlet;

    public HelpCanvas(TetrisMidlet m){ midlet=m; }

    protected void paint(Graphics g){
        g.setColor(0,0,40);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(255,230,0);
        g.drawString("HOW TO PLAY", getWidth()/2, 20, Graphics.HCENTER);

        g.setColor(255,255,255);
        g.drawString("LEFT/RIGHT : move",5,60,0);
        g.drawString("FIRE : rotate",5,90,0);
        g.drawString("DOWN : drop fast",5,120,0);
        g.drawString("0 : pause / resume",5,150,0);

        g.drawString("Press BACK", getWidth()/2, getHeight()-18, Graphics.HCENTER);
    }

    protected void keyPressed(int k){
        midlet.backToMenu();
    }
}
