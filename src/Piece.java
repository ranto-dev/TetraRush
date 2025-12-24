public class Piece {

    public int[][] blocks = new int[4][2];
    public int x,y;

    private Piece(int[][] b){
        for(int i=0;i<4;i++){
            blocks[i][0]=b[i][0];
            blocks[i][1]=b[i][1];
        }
    }

    private static int rnd(int m){
        return (int)(System.currentTimeMillis()%m);
    }

    public static Piece randomPiece(){
        switch(rnd(7)){
            case 0: return new Piece(new int[][]{{0,0},{1,0},{0,1},{1,1}});
            case 1: return new Piece(new int[][]{{0,0},{1,0},{2,0},{3,0}});
            case 2: return new Piece(new int[][]{{0,0},{1,0},{2,0},{2,1}});
            case 3: return new Piece(new int[][]{{0,0},{1,0},{2,0},{0,1}});
            case 4: return new Piece(new int[][]{{0,0},{1,0},{1,1},{2,1}});
            case 5: return new Piece(new int[][]{{0,1},{1,1},{1,0},{2,0}});
            default:return new Piece(new int[][]{{0,0},{1,0},{1,1},{2,0}});
        }
    }
}
