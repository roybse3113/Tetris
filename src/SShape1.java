public class SShape1 extends Shape implements ShapeFields {
    static int[][] coords = { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } };
    static int[][] turnCoords;
    static int cnt = 1;
    Tetrominoes piece;
    static Shape result;
    
    public SShape1(Tetrominoes t) {
        super(t); 
        piece = t;
    }
    
    public int[][] getCoords() {
        return coords;
    }
    
    @Override
    public Shape rotateLeft() {

        result = new Shape();
        result.setShape(piece);
        
        if (cnt == 1) {
            turn1();
        }
        if (cnt == 2) {
            turn2();
        }
        if (cnt == 3) {
            turn3();
        }
        if (cnt == 4) {
            turn4();
            cnt = 0;
        }
            
        result.setCnt();
        
        for (int i = 0; i < 4; i++) {          
            result.setX(i, coordsCopy[i][0]);
            result.setY(i, coordsCopy[i][1]);     
        } 
        return result;      
    }
    
    public void turn1() {
        turnCoords = new int[][] { { -1, 0 }, { 0, 0 }, { 0, -1 }, { 1, -1 } };
        for (int i = 0; i < 4; i++) { 
            setXShape(i, turnCoords[i][0]);
            setYShape(i, turnCoords[i][1]);
        }
    }
    public void turn2() {
        turnCoords = new int[][] { { 0, 1 }, { 0, 0 }, { -1, 0 }, { -1, -1 } };
        for (int i = 0; i < 4; i++) {
            setXShape(i, turnCoords[i][0]);
            setYShape(i, turnCoords[i][1]);
        }
    }
    public void turn3() {
        turnCoords = new int[][] { { 1, 0 }, { 0, 0 }, { 0, 1 }, { -1, 1 } };
        for (int i = 0; i < 4; i++) {
            setXShape(i, turnCoords[i][0]);
            setYShape(i, turnCoords[i][1]);
        }
    }
    public void turn4() {
        turnCoords = new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } };
        for (int i = 0; i < 4; i++) {
            setXShape(i, turnCoords[i][0]);
            setYShape(i, turnCoords[i][1]);
        }
    }
    
}

