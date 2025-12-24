import javax.microedition.lcdui.*;

public class GameCanvasTetris extends Canvas implements Runnable {

    private static final int STATE_PLAY = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_GAME_OVER = 3;

    private int gameState = STATE_PLAY;
    private TetrisMidlet midlet;
    private boolean running = true;

    private int COLS = 10;
    private int ROWS = 18;
    private int CELL = 14;

    private int[][] board = new int[ROWS][COLS];

    private Piece current;
    private Piece nextPiece;
    private int curX, curY;

    private long tick = 0;
    private int score = 0;
    private int lines = 0;

    public GameCanvasTetris(TetrisMidlet m) {
        this.midlet = m;
        current = Piece.random();
        nextPiece = Piece.random();
        curX = COLS / 2 - 1;
        curY = 0;
        new Thread(this).start();
    }

    public void run() {
        while (running) {
            if (gameState == STATE_PLAY) {
                long t = System.currentTimeMillis();
                if (t - tick > 500) {
                    tick = t;
                    drop();
                }
            }
            repaint();
            serviceRepaints();
            try { Thread.sleep(60); } catch (Exception e) {}
        }
    }

    private void spawnPiece() {
        current = nextPiece;
        nextPiece = Piece.random();
        curX = COLS / 2 - 1;
        curY = 0;
        if (collides(curX, curY, current)) gameState = STATE_GAME_OVER;
    }

    private boolean collides(int x, int y, Piece p) {
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++) {
            if (p.shape[r][c] == 1) {
                int nx = x + c;
                int ny = y + r;
                if (nx < 0 || nx >= COLS || ny >= ROWS) return true;
                if (ny >= 0 && board[ny][nx] != 0) return true;
            }
        }
        return false;
    }

    private void mergePiece() {
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++) {
            if (current.shape[r][c] == 1) {
                int nx = curX + c;
                int ny = curY + r;
                if (ny >= 0) board[ny][nx] = current.type;
            }
        }
        clearLines();
        spawnPiece();
    }

    private void clearLines() {
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == 0) full = false;
            if (full) {
                lines++;
                score += 100;
                for (int y = r; y > 0; y--)
                    for (int x = 0; x < COLS; x++)
                        board[y][x] = board[y - 1][x];
                r++;
            }
        }
    }

    private void drop() {
        if (!collides(curX, curY + 1, current)) curY++;
        else mergePiece();
    }

    private void move(int dx) {
        if (!collides(curX + dx, curY, current)) curX += dx;
    }

    private void rotate() {
        Piece t = current.rotated();
        if (!collides(curX, curY, t)) current = t;
    }

    protected void keyPressed(int k) {
        if (gameState == STATE_GAME_OVER) {
            int a = getGameAction(k);
            if (a == FIRE) midlet.restartGame();
            if (k == KEY_POUND) midlet.backToMenu();
            return;
        }
        if (gameState == STATE_PAUSE) {
            int a = getGameAction(k);
            if (a == FIRE) gameState = STATE_PLAY;
            if (k == KEY_POUND) midlet.backToMenu();
            return;
        }
        if (k == KEY_STAR) {
            gameState = (gameState == STATE_PLAY) ? STATE_PAUSE : STATE_PLAY;
            return;
        }
        if (gameState != STATE_PLAY) return;

        int a = getGameAction(k);
        if (a == LEFT) move(-1);
        else if (a == RIGHT) move(1);
        else if (a == DOWN) drop();
        else if (a == FIRE) rotate();

        if (k == KEY_POUND) midlet.backToMenu();
    }

    protected void paint(Graphics g) {
        // Fond général
        g.setColor(20, 20, 40);
        g.fillRect(0, 0, getWidth(), getHeight());

        int gridX = 10;
        int gridY = 10;
        int gridWidth = COLS * CELL;
        int gridHeight = ROWS * CELL;

        // Fond de la grille
        g.setColor(0, 0, 60);
        g.fillRect(gridX, gridY, gridWidth, gridHeight);

        // Bordure de la grille
        g.setColor(255, 255, 255);
        g.drawRect(gridX - 1, gridY - 1, gridWidth + 1, gridHeight + 1);

        // Lignes internes
        g.setColor(100, 100, 120);
        for (int r = 1; r < ROWS; r++)
            g.drawLine(gridX, gridY + r * CELL, gridX + gridWidth, gridY + r * CELL);
        for (int c = 1; c < COLS; c++)
            g.drawLine(gridX + c * CELL, gridY, gridX + c * CELL, gridY + gridHeight);

        // Dessiner pièces
        drawBoard(g, gridX, gridY);
        drawPiece(g, gridX, gridY);

        // HUD
        g.setColor(255, 255, 0);
        g.drawString("Score: " + score, gridX + gridWidth + 10, gridY, Graphics.TOP | Graphics.LEFT);
        g.drawString("Lines: " + lines, gridX + gridWidth + 10, gridY + 20, Graphics.TOP | Graphics.LEFT);
        g.drawString(" - Pause - *", gridX + gridWidth + 10, gridY + 40, Graphics.TOP | Graphics.LEFT);
        g.drawString(" - Menu - #", gridX + gridWidth + 10, gridY + 60, Graphics.TOP | Graphics.LEFT);

        // Prochaine pièce
        drawNextPiece(g, gridX + gridWidth + 10, gridY + 80);

        // Pause overlay
        if (gameState == STATE_PAUSE) {
            g.setColor(0, 0, 0);
            g.fillRect(30, 80, 120, 40);
            g.setColor(255, 255, 0);
            g.drawString("PAUSED", 90, 100, Graphics.HCENTER | Graphics.BASELINE);
        }

        // Game Over overlay clignotant
        if (gameState == STATE_GAME_OVER) {
            int rectWidth = 200;
            int rectHeigth = 100;
            g.setColor(200,0,0);
            g.fillRect((getWidth() / 2) - (rectWidth / 2) , (getHeight() / 2) - (rectHeigth / 2), rectWidth, rectHeigth);
            g.setColor(255,255, 0);
            g.drawString("GAME OVER", getWidth() / 2, getHeight() / 2 - 10, Graphics.HCENTER | Graphics.BASELINE);
            g.setColor(255,255,255);
            g.drawString("Press FIRE touch to restart", getWidth() / 2, getHeight() / 2 + 15, Graphics.HCENTER | Graphics.BASELINE);
        }
    }

    private void drawBoard(Graphics g, int offsetX, int offsetY) {
        for (int r = 0; r < ROWS; r++)
        for (int c = 0; c < COLS; c++) {
            if (board[r][c] != 0) {
                g.setColor(getColor(board[r][c]));
                g.fillRect(offsetX + c * CELL, offsetY + r * CELL, CELL, CELL);
                g.setColor(0, 0, 0);
                g.drawRect(offsetX + c * CELL, offsetY + r * CELL, CELL, CELL);
            }
        }
    }

    // Draw piece function
    private void drawPiece(Graphics g, int offsetX, int offsetY) {
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++)
            if (current.shape[r][c] == 1) {
                g.setColor(getColor(current.type));
                g.fillRect(offsetX + (curX + c) * CELL, offsetY + (curY + r) * CELL, CELL, CELL);
                g.setColor(0, 0, 0);
                g.drawRect(offsetX + (curX + c) * CELL, offsetY + (curY + r) * CELL, CELL, CELL);
            }
    }

    // Drow next piace function
    private void drawNextPiece(Graphics g, int offsetX, int offsetY) {
        g.setColor(255, 255, 255);
        g.drawString("Next:", offsetX, offsetY, Graphics.TOP | Graphics.LEFT);
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++)
            if (nextPiece.shape[r][c] == 1) {
                g.setColor(getColor(nextPiece.type));
                g.fillRect(offsetX + c * CELL, offsetY + 15 + r * CELL, CELL, CELL);
                g.setColor(0, 0, 0);
                g.drawRect(offsetX + c * CELL, offsetY + 15 + r * CELL, CELL, CELL);
            }
    }

    // Get color appropriate to piece function
    private int getColor(int t) {
        switch (t) {
            case 1: return 0xFF0000; // red
            case 2: return 0x00FF00; // green
            case 3: return 0x0000FF; // blue
            case 4: return 0xFFFF00; // yellow
            case 5: return 0xFF00FF; // magenta
            case 6: return 0x00FFFF; // cyan
            case 7: return 0xFFA500; // orange
            default: return 0xFFFFFF; // white
        }
    }
}
