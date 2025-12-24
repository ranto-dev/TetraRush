import javax.microedition.lcdui.*;

public class MenuCanvas extends Canvas implements Runnable {

    private TetrisMidlet midlet;
    private int selected = 0;
    private boolean running = true;
    private int glow = 0;

    private String[] options = {"PLAY GAME", "ABOUT", "HELP"};

    public MenuCanvas(TetrisMidlet m){
        this.midlet = m;
        new Thread(this).start();
    }

    public void run(){
        while(running){
            glow = (glow + 8) % 255;
            repaint();
            try { Thread.sleep(80); } catch(Exception e){}
        }
    }

    protected void paint(Graphics g){
        int w = getWidth(), h = getHeight();

        // gradient background
        g.setColor(10,10,30);
        g.fillRect(0,0,w,h);
        g.setColor(40,0,80);
        g.fillRect(0,h/2,w,h);

        // game title
        g.setColor(255,200,0);
        g.drawString("TETRIS", w/2, 25, Graphics.HCENTER);

        // animated ring
        g.setColor(100,100+glow,255);
        g.fillArc(w/2-25, 55, 50,50, 0, glow*2);

        // menu options
        for(int i=0;i<options.length;i++){
            if(i==selected){
                g.setColor(0,255,140);
                g.fillRoundRect(w/2-55,120+i*28,110,22,8,8);
                g.setColor(0,0,0);
            } else g.setColor(255,255,255);
            g.drawString(options[i], w/2,130+i*28,Graphics.HCENTER);
        }

        g.setColor(180,180,180);
        g.drawString("UP/DOWN + FIRE", w/2, h-18, Graphics.HCENTER);
    }

    protected void keyPressed(int key){
        int ga = getGameAction(key);

        if(ga==UP && selected>0) selected--;
        if(ga==DOWN && selected<options.length-1) selected++;

        if(ga==FIRE){
            if(selected==0) midlet.showGame();
            if(selected==1) midlet.showAbout();
            if(selected==2) midlet.showHelp();
        }
        repaint();
    }
}
