import java.util.ArrayList;
import java.util.Collections;

public class Shape implements ShapeFields {
    private Tetrominoes pieceShape;
    private int[][] coords;
    static  int[][] coordsCopy;
    private ArrayList<Integer> next = new ArrayList<Integer>();
    private ArrayList<Tetrominoes> allPieces = new ArrayList<Tetrominoes>();
    private Tetrominoes[] values = Tetrominoes.values();
    private Shape result;
    
    public Shape() {
        coords = new int[4][2];
        coordsCopy = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }
  
    public Shape(Tetrominoes t) {
        t = pieceShape;
    }
  
  
    public void setShape(Tetrominoes shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                int[][] c = shape.getCoords();
                coords[i][j] = c[i][j];
                coordsCopy[i][j] = c[i][j];
            }
        }
 
        pieceShape = shape;
    }
 
    public void setX(int index, int x) {
        coords[index][0] = x;
        coordsCopy[index][0] = x;
    }
  
    public void setY(int index, int y) {
        coords[index][1] = y;
    }
  
  //Static copy for setX for testing
    static void setXShape(int index, int x) {
        coordsCopy[index][0] = x;
    }
  
  //Static copy of setY for testing
    static void setYShape(int index, int y) {
        coordsCopy[index][1] = y;
    }
  
  //Static copy of y for testing
    static int yShape(int index) {
        return coordsCopy[index][1];
    }
  
  //Static copy of x for testing
    static int xShape(int index) {
        return coordsCopy[index][0];
    }
  
  //Sets count to keep track of orientation for each shape
    public void setCnt() {
        if (pieceShape == Tetrominoes.LShape) {
            LShape1.cnt++;
        }
        if (pieceShape == Tetrominoes.LineShape) {
            LineShape1.cnt++;
        }
        if (pieceShape == Tetrominoes.SShape) {
            SShape1.cnt++;
        }
        if (pieceShape == Tetrominoes.TShape) {
            TShape1.cnt++;
        }
        if (pieceShape == Tetrominoes.ZShape) {
            ZShape1.cnt++;
        }
        if (pieceShape == Tetrominoes.MirroredLShape) {
            MirroredLShape1.cnt++;
        }
     
    }
  
    public int x(int index) {
        return coords[index][0];
    }
  
    public int y(int index) {
        return coords[index][1];
    }
 
    public Tetrominoes getShape() {
        return pieceShape;
    }
  
  //Prepare the next random piece
    public void setRandomShape() {
        if (next.isEmpty()) {
            Collections.addAll(next, 1,2,3,4,5,6,7);
            Collections.shuffle(next);
        }
        int n = next.get(0);
        setShape(values[n]);
        allPieces.add(pieceShape);
        next.remove(0);
    }
  
    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < coords.length; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    @Override
    public Shape rotateLeft() {
     
        SShape1 s = new SShape1(pieceShape); 
        LShape1 l = new LShape1(pieceShape);
        MirroredLShape1 m = new MirroredLShape1(pieceShape);
        LineShape1 li = new LineShape1(pieceShape);
        TShape1 t = new TShape1(pieceShape);
        ZShape1 z = new ZShape1(pieceShape);
    
    //Square shape returns same
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }

        result = new Shape();
        result.pieceShape = pieceShape;
    
    //Rotates by each shapes
        if (pieceShape == Tetrominoes.SShape) {
            s.rotateLeft();
        }
    
        if (pieceShape == Tetrominoes.LShape) {
            l.rotateLeft();
        }
    
        if (pieceShape == Tetrominoes.MirroredLShape) {
            m.rotateLeft();
        }
        if (pieceShape == Tetrominoes.LineShape) {
            li.rotateLeft();
        }
    
        if (pieceShape == Tetrominoes.TShape) {
            t.rotateLeft();
        }
    
        if (pieceShape == Tetrominoes.ZShape) {
            z.rotateLeft();
        }
    
        for (int i = 0; i < 4; i++) {      
            result.setX(i, coordsCopy[i][0]);
            result.setY(i, coordsCopy[i][1]);
        }
 
        return result;
    
    }

 
}
