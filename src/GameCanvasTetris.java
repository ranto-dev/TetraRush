import javax.microedition.lcdui.*;

public class GameCanvasTetris extends Canvas implements Runnable {

    private boolean running = true;
    private boolean paused = false;
    private boolean gameOver = false;

    private int cols = 10;
    private int rows = 18;
    private int cell = 12;

    private int[][] board = new int[rows][cols];
    private Piece current;

    private long lastFall = 0;
    private int fallDelay = 550;

    private int score = 0;
    private int cleared = 0;

    private TetrisMidlet midlet;

    public GameCanvasTetris(TetrisMidlet m) {
        this.midlet = m;
        spawnPiece();
        new Thread(this).start();
    }

    public void run() {
        while(running) {

            if(!paused && !gameOver) {
                long now = System.currentTimeMillis();
                if(now - lastFall > fallDelay) {
                    if(canMove(0,1)) current.y++;
                    else lockPiece();
                    lastFall = now;
                }
            }

            repaint();
            serviceRepaints();

            try { Thread.sleep(40); } catch(Exception e){}
        }
    }

    protected void paint(Graphics g) {

        // Background gradient style
        for(int i=0;i<getHeight();i++){
            g.setColor(0,0,i/2);
            g.drawLine(0,i,getWidth(),i);
        }

        drawFrame(g);
        drawGrid(g);
        drawBoard(g);
        drawPiece(g);

        drawHUD(g);

        if(paused) drawPause(g);
        if(gameOver) drawGameOver(g);
    }

    private void drawFrame(Graphics g){
        g.setColor(40,40,60);
        g.fillRect(0,0,cols*cell+6,rows*cell+6);

        g.setColor(0,0,0);
        g.fillRect(3,3,cols*cell,rows*cell);
    }

    private void drawGrid(Graphics g){
        g.setColor(30,30,40);
        for(int c=0;c<=cols;c++)
            g.drawLine(c*cell+3, 3, c*cell+3, rows*cell+3);
        for(int r=0;r<=rows;r++)
            g.drawLine(3, r*cell+3, cols*cell+3, r*cell+3);
    }

    private void drawBlock(Graphics g,int x,int y,int r,int gr,int b){
        g.setColor(r,gr,b);
        g.fillRect(x,y,cell,cell);

        g.setColor(255,255,255);
        g.drawLine(x,y,x+cell,y);
        g.drawLine(x,y,x,y+cell);

        g.setColor(0,0,0);
        g.drawLine(x,y+cell-1,x+cell,y+cell-1);
        g.drawLine(x+cell-1,y,x+cell-1,y+cell);
    }

    private void drawBoard(Graphics g){
        for(int r=0;r<rows;r++)
            for(int c=0;c<cols;c++)
                if(board[r][c]!=0)
                    drawBlock(g,c*cell+3,r*cell+3,0,150,255);
    }

    private void drawPiece(Graphics g){
        g.setColor(255,80,80);
        for(int i=0;i<4;i++){
            int px=current.blocks[i][0]+current.x;
            int py=current.blocks[i][1]+current.y;
            if(py>=0)
                drawBlock(g,px*cell+3,py*cell+3,255,80,80);
        }
    }

    private void drawHUD(Graphics g){
        g.setColor(255,255,0);
        g.drawString("Score: "+score, cols*cell+10, 10, Graphics.LEFT|Graphics.TOP);
        g.drawString("Lines: "+cleared, cols*cell+10, 28, Graphics.LEFT|Graphics.TOP);
        g.drawString("0=P Pause", cols*cell+10, 48, Graphics.LEFT|Graphics.TOP);
        g.drawString("# Quit", cols*cell+10, 66, Graphics.LEFT|Graphics.TOP);
    }

    private void drawPause(Graphics g){
        g.setColor(255,255,0);
        g.drawString("PAUSED", getWidth()/2, getHeight()/2,
                Graphics.HCENTER | Graphics.BASELINE);
    }

    private void drawGameOver(Graphics g){
        int t = (int)((System.currentTimeMillis()/200)%2);

        if(t==0) g.setColor(255,0,0);
        else g.setColor(255,200,0);

        g.drawString("GAME OVER", getWidth()/2, getHeight()/2 - 10,
                Graphics.HCENTER | Graphics.BASELINE);

        g.setColor(255,255,255);
        g.drawString("Press FIRE to restart",
                getWidth()/2, getHeight()/2 + 15,
                Graphics.HCENTER | Graphics.BASELINE);
    }

    protected void keyPressed(int k){
        if(gameOver){
            if(getGameAction(k)==FIRE) midlet.restartGame();
            return;
        }

        int a=getGameAction(k);

        if(a==LEFT && canMove(-1,0)) current.x--;
        if(a==RIGHT && canMove(1,0)) current.x++;
        if(a==DOWN && canMove(0,1)) current.y++;

        if(a==FIRE) rotate();

        if(k==KEY_NUM0) paused=!paused;

        if(k==KEY_POUND) midlet.exit();
    }

    private boolean canMove(int dx,int dy){
        for(int i=0;i<4;i++){
            int nx=current.blocks[i][0]+current.x+dx;
            int ny=current.blocks[i][1]+current.y+dy;

            if(nx<0||nx>=cols||ny>=rows) return false;
            if(ny>=0 && board[ny][nx]!=0) return false;
        }
        return true;
    }

    private void lockPiece(){
        for(int i=0;i<4;i++){
            int nx=current.blocks[i][0]+current.x;
            int ny=current.blocks[i][1]+current.y;
            if(ny>=0) board[ny][nx]=1;
        }
        clearLines();
        spawnPiece();

        if(!canMove(0,0)){
            gameOver=true;
            paused=false;
        }
    }

    private void clearLines(){
        for(int r=rows-1;r>=0;r--){
            boolean full=true;
            for(int c=0;c<cols;c++)
                if(board[r][c]==0){ full=false; break; }

            if(full){
                score += 100;
                cleared++;

                for(int y=r;y>0;y--)
                    for(int c=0;c<cols;c++)
                        board[y][c]=board[y-1][c];

                r++;
            }
        }
    }

    private void rotate(){
        int[][] tmp=new int[4][2];
        for(int i=0;i<4;i++){
            tmp[i][0]=-current.blocks[i][1];
            tmp[i][1]= current.blocks[i][0];
        }

        int[][] old=current.blocks;
        current.blocks=tmp;
        if(!canMove(0,0)) current.blocks=old;
    }

    private void spawnPiece(){
        current=Piece.randomPiece();
        current.x=cols/2-1;
        current.y=-2;
    }
}
