/**
 * Classe: Pièce
 */
public class Piece {

    // Déclaration des attributs
    public int[][] shape = new int[4][4];
    public int type;

    // Constructeur de la classe Piece
    private Piece(int[][] s, int t) {
        type = t;
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++)
            shape[r][c] = s[r][c];
    }

    /**
     * Les méthodes de la classe Piece
     */

    // Rotation d'une pièce
    public Piece rotated() {
        int[][] n = new int[4][4];
        for (int r = 0; r < 4; r++)
        for (int c = 0; c < 4; c++)
            n[c][3 - r] = shape[r][c];
        return new Piece(n, type);
    }

    /**
     * fonction qu s'occupe du choix aléatoire d'une pièce
     */
    private static int rand(int n) {
        return (int) ((System.currentTimeMillis() % n));
    }

    public static Piece random() {
        int t = rand(7) + 1;

        // Déclaration de tous les formes de pièce possible dans le jeu Tetris
        switch (t) {
            case 1: return new Piece(new int[][]{
                {0,1,0,0},{0,1,0,0},{0,1,0,0},{0,1,0,0}}, t); // la pièce I
            case 2: return new Piece(new int[][]{
                {1,1,0,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce 0
            case 3: return new Piece(new int[][]{
                {1,1,0,0},{0,1,1,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce Z
            case 4: return new Piece(new int[][]{
                {0,1,1,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce S
            case 5: return new Piece(new int[][]{
                {1,0,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce L
            case 6: return new Piece(new int[][]{
                {0,0,1,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce J
            default: return new Piece(new int[][]{
                {0,1,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}}, t); // la pièce T
        }
    }
}
