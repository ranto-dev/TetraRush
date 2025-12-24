import javax.microedition.lcdui.*;

public class GameCanvasTetris extends Canvas implements Runnable {

    private static final int STATE_MENU = 0;
    private static final int STATE_PLAY = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_GAME_OVER = 3;

    private int gameState = STATE_PLAY;

    private TetrisMidlet midlet;
    private boolean running = true;

    private int COLS = 10;
    private int ROWS = 18;
    private int cell = 10;

    private int[][] board = new int[ROWS][COLS];

    private Piece current;
    private int curX, curY;

    private long tick = 0;
    private int score = 0;

    public GameCanvasTetris(TetrisMidlet m) {
        this.midlet = m;
        spawnPiece();
        new Thread(this).start();
    }

    public void run() {
        while(running) {

            if(gameState == STATE_PLAY) {
                long t = System.currentTimeMillis();
                if(t - tick > 500) {
                    tick = t;
                    drop();
                }
            }

            repaint();
            serviceRepaints();

            try { Thread.sleep(60); } catch(Exception e){}
        }
    }

    private void spawnPiece() {
        current = Piece.random();
        curX = COLS/2 - 1;
        curY = 0;

        if(collides(curX, curY, current)) {
            gameState = STATE_GAME_OVER;
        }
    }

    private boolean collides(int x, int y, Piece p) {
        for(int r=0;r<4;r++)
        for(int c=0;c<4;c++) {
            if(p.shape[r][c]==1) {
                int nx=x+c, ny=y+r;
                if(ny<0 || ny>=ROWS || nx<0 || nx>=COLS) return true;
                if(board[ny][nx]==1) return true;
            }
        }
        return false;
    }

    private void mergePiece() {
        for(int r=0;r<4;r++)
        for(int c=0;c<4;c++) {
            if(current.shape[r][c]==1) {
                board[curY+r][curX+c]=1;
            }
        }
        clearLines();
        spawnPiece();
    }

    private void clearLines() {
        for(int r=0;r<ROWS;r++) {
            boolean full=true;
            for(int c=0;c<COLS;c++)
                if(board[r][c]==0) full=false;

            if(full){
                score+=100;
                for(int y=r;y>0;y--)
                    for(int x=0;x<COLS;x++)
                        board[y][x]=board[y-1][x];
            }
        }
    }

    private void drop() {
        if(!collides(curX, curY+1, current)) curY++;
        else mergePiece();
    }

    private void move(int dx) {
        if(!collides(curX+dx, curY, current)) curX+=dx;
    }

    private void rotate() {
        Piece t = current.rotated();
        if(!collides(curX, curY, t)) current = t;
    }

    protected void keyPressed(int k) {

        // GAME OVER
        if(gameState == STATE_GAME_OVER) {
            int a=getGameAction(k);
            if(a==FIRE) midlet.restartGame();
            if(k==KEY_POUND) midlet.backToMenu();
            return;
        }

        // PAUSE
        if(gameState == STATE_PAUSE) {
            int a=getGameAction(k);
            if(a==FIRE) gameState = STATE_PLAY;
            if(k==KEY_POUND) midlet.backToMenu();
            return;
        }

        // TOGGLE PAUSE
        if(k==KEY_STAR) {
            gameState = (gameState==STATE_PLAY)?STATE_PAUSE:STATE_PLAY;
            return;
        }

        // PLAY INPUT ONLY WHEN PLAYING
        if(gameState != STATE_PLAY) return;

        int a=getGameAction(k);

        if(a==LEFT) move(-1);
        else if(a==RIGHT) move(1);
        else if(a==DOWN) drop();
        else if(a==FIRE) rotate();

        if(k==KEY_POUND) midlet.backToMenu();
    }

    protected void paint(Graphics g) {

        g.setColor(0,0,30);
        g.fillRect(0,0,getWidth(),getHeight());

        drawBoard(g);
        drawPiece(g);

        g.setColor(255,255,0);
        g.drawString("Score: "+score, 2, 2, Graphics.TOP|Graphics.LEFT);

        if(gameState == STATE_PAUSE) {
            g.setColor(0,0,0);
            g.fillRect(20,60,100,40);
            g.setColor(255,255,0);
            g.drawString("PAUSED",70,80,Graphics.HCENTER|Graphics.BASELINE);
            return;
        }

        if(gameState == STATE_GAME_OVER) {
            g.setColor(200,0,0);
            g.fillRect(10,60,120,50);
            g.setColor(255,255,0);
            g.drawString("GAME OVER",70,75,Graphics.HCENTER|Graphics.BASELINE);
            g.drawString("FIRE=Restart",70,95,Graphics.HCENTER|Graphics.BASELINE);
        }
    }

    private void drawBoard(Graphics g) {
        for(int r=0;r<ROWS;r++)
        for(int c=0;c<COLS;c++) {
            if(board[r][c]==1){
                g.setColor(0,180,255);
                g.fillRect(c*cell,r*cell,cell,cell);
                g.setColor(0,0,80);
                g.drawRect(c*cell,r*cell,cell,cell);
            }
        }
    }

    private void drawPiece(Graphics g) {
        g.setColor(255,120,0);
        for(int r=0;r<4;r++)
        for(int c=0;c<4;c++)
            if(current.shape[r][c]==1)
                g.fillRect((curX+c)*cell,(curY+r)*cell,cell,cell);
    }
}
