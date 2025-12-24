import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class GameCanvasTetris extends GameCanvas implements Runnable {

    private TetrisMidlet midlet;
    private boolean running=true, paused=false;

    private int gameOverAlpha = 0;
    private boolean isGameOver = false;

    private int[][] grid = new int[20][10];
    private Block current;
    private int score=0, level=1, lines=0;

    public GameCanvasTetris(TetrisMidlet m){
        super(true);
        midlet=m;
        spawnBlock();
        new Thread(this).start();
    }

    public void run(){
        while(running){
            if(!paused){
                tick();
                repaint();
            }
            try{ Thread.sleep(Math.max(120,400-(level*40))); }
            catch(Exception e){}
        }
    }

    private void spawnBlock(){
        int[][][] shapes = Blocks[random(0,6)];
        int color = Colors[random(0,7)];
        current = new Block(shapes,color);
    }

    private void tick(){
        if(canMove(0,1)) current.y++;
        else {
            lockPiece();
            clearLines();
            spawnBlock();
            if(!canMove(0,0)) gameOver();
        }
    }


    private void gameOver(){
        paused = true;
        isGameOver = true;
        gameOverAlpha = 0;
    }


    private void drawGameOver(Graphics g){
        if(!isGameOver) return;

        // Semi-transparent overlay
        if(gameOverAlpha < 180) gameOverAlpha += 6;
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());

        int pulse = (int)(Math.abs(Math.sin(System.currentTimeMillis() / 200.0)) * 255);

        g.setColor(255, 60 + pulse/4, 60);
        g.drawString("GAME OVER", getWidth()/2, getHeight()/2 - 10, Graphics.HCENTER);

        g.setColor(200,200,200);
        g.drawString("Press FIRE to retry", getWidth()/2, getHeight()/2 + 20, Graphics.HCENTER);
    }

    private void lockPiece(){
        int[][] s = current.shape();
        for(int r=0;r<s.length;r++)
            for(int c=0;c<s[0].length;c++)
                if(s[r][c]==1)
                    grid[current.y+r][current.x+c]=current.color;
    }

    private void clearLines(){
        int cleared=0;
        for(int r=0;r<20;r++){
            boolean full=true;
            for(int c=0;c<10;c++)
                if(grid[r][c]==0) full=false;

            if(full){
                cleared++;
                for(int y=r;y>0;y--)
                    for(int x=0;x<10;x++)
                        grid[y][x]=grid[y-1][x];
            }
        }

        if(cleared>0){
            score += cleared*100;
            lines += cleared;
            if(lines/5 >= level) level++;
        }
    }

    private boolean canMove(int dx,int dy){
        int[][] s=current.shape();
        for(int r=0;r<s.length;r++)
            for(int c=0;c<s[0].length;c++)
                if(s[r][c]==1){
                    int nx=current.x+c+dx;
                    int ny=current.y+r+dy;
                    if(nx<0||nx>=10||ny>=20) return false;
                    if(ny>=0 && grid[ny][nx]!=0) return false;
                }
        return true;
    }

    protected void keyPressed(int k){
        int g=getGameAction(k);

        if(k==KEY_POUND){ midlet.backToMenu(); return; }
        if(k==KEY_NUM0){ paused=!paused; repaint(); return; }

        if(paused) return;

        if(g==LEFT && canMove(-1,0)) current.x--;
        if(g==RIGHT && canMove(1,0)) current.x++;
        if(g==DOWN && canMove(0,1)) current.y++;
        if(g==FIRE){
            current.rotate();
            if(!canMove(0,0)) current.rotate(); // revert if blocked
        }

        repaint();
    }

    private void resetGame(){
        grid = new int[20][10];
        score = 0; level = 1; lines = 0;
        isGameOver = false;
        paused = false;
        spawnBlock();
    }

    protected void paint(Graphics g){
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(),getHeight());

        drawBoard(g);
        drawUI(g);
        drawGameOver(g);

        if(paused){
            g.setColor(255,0,0);
            g.drawString("PAUSED", getWidth()/2, getHeight()/2, Graphics.HCENTER);
        }
    }

    private void drawBoard(Graphics g){
        int ox=20, oy=20, size=12;

        // frame
        g.setColor(120,120,120);
        g.drawRect(ox-3,oy-3,10*size+6,20*size+6);

        // grid blocks
        for(int y=0;y<20;y++)
            for(int x=0;x<10;x++)
                if(grid[y][x]!=0){
                    g.setColor(grid[y][x]);
                    g.fillRect(ox+x*size, oy+y*size, size-1,size-1);
                }

        // current piece
        int[][] s=current.shape();
        for(int r=0;r<s.length;r++)
            for(int c=0;c<s[0].length;c++)
                if(s[r][c]==1){
                    g.setColor(current.color);
                    g.fillRect(ox+(current.x+c)*size, oy+(current.y+r)*size, size-1,size-1);
                }
    }

    private void drawUI(Graphics g){
        g.setColor(255,255,255);
        g.drawString("Score: "+score, 5, 4, 0);
        g.drawString("Lvl: "+level, 100, 4, 0);
    }

    private static int random(int a,int b){
        return (int)(Math.random()*(b-a+1))+a;
    }

    // --- PIECES & COLORS --------------------------

    private static final int[][][][] Blocks = {

        // I
        {{{1,1,1,1}},
         {{1},{1},{1},{1}}},

        // O
        {{{1,1},{1,1}}},

        // T
        {{{1,1,1},{0,1,0}},
         {{1,0},{1,1},{1,0}},
         {{0,1,0},{1,1,1}},
         {{0,1},{1,1},{0,1}}},

        // L
        {{{1,1,1},{1,0,0}},
         {{1,1},{0,1},{0,1}},
         {{0,0,1},{1,1,1}},
         {{1,0},{1,0},{1,1}}},

        // J
        {{{1,1,1},{0,0,1}},
         {{0,1},{0,1},{1,1}},
         {{1,0,0},{1,1,1}},
         {{1,1},{1,0},{1,0}}},

        // S
        {{{0,1,1},{1,1,0}},
         {{1,0},{1,1},{0,1}}},

        // Z
        {{{1,1,0},{0,1,1}},
         {{0,1},{1,1},{1,0}}}
    };

    private static final int[] Colors = {
        0x00CCFF, 0xFFCC00, 0xFF6699,
        0x66FF33, 0xFF6600, 0x9999FF, 0xFF3366
    };
}
