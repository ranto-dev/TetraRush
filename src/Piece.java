public class Piece {

    public int[][] shape = new int[4][4];

    private Piece(int[][] s){
        for(int r=0;r<4;r++)
        for(int c=0;c<4;c++)
            shape[r][c]=s[r][c];
    }

    public Piece rotated() {
        int[][] n = new int[4][4];
        for(int r=0;r<4;r++)
        for(int c=0;c<4;c++)
            n[c][3-r]=shape[r][c];
        return new Piece(n);
    }

    private static int rand(int n){
        return (int)((System.currentTimeMillis()%n));
    }

    public static Piece random() {
        int t = rand(7);
        switch(t){
            case 0: return new Piece(new int[][]{
                {0,1,0,0},{0,1,0,0},{0,1,0,0},{0,1,0,0}});
            case 1: return new Piece(new int[][]{
                {1,1,0,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}});
            case 2: return new Piece(new int[][]{
                {1,1,0,0},{0,1,1,0},{0,0,0,0},{0,0,0,0}});
            case 3: return new Piece(new int[][]{
                {0,1,1,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}});
            case 4: return new Piece(new int[][]{
                {1,0,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}});
            case 5: return new Piece(new int[][]{
                {0,0,1,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}});
            default: return new Piece(new int[][]{
                {0,1,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}});
        }
    }
}
