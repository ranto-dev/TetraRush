public class Block {

    public int[][][] shapes;
    public int rotation = 0;
    public int x = 3, y = 0;
    public int color;

    public Block(int[][][] s, int c){
        shapes = s;
        color = c;
    }

    public int[][] shape(){
        return shapes[rotation];
    }

    public void rotate(){
        rotation = (rotation+1) % shapes.length;
    }
}
