import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public enum Tetrominoes {
       
    NoShape(NoShape1.coords, colorChoose()),
    ZShape(ZShape1.coords, colorChoose()),
    SShape(SShape1.coords, colorChoose()),
    LineShape(LineShape1.coords, colorChoose()),
    TShape(TShape1.coords, colorChoose()),
    SquareShape(SquareShape1.coords, colorChoose()),
    LShape(LShape1.coords, colorChoose()),
    MirroredLShape(MirroredLShape1.coords, colorChoose());
       
    private int[][] coords;
    private Color color;
    
    public int[][] getCoords() {
        return coords;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static Color colorChoose() {
        Color co;
        ArrayList<Integer> colors = new ArrayList<Integer>();
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            int n = r.nextInt(256);
            colors.add(n);
        }  
        co = new Color(colors.get(0),colors.get(1),colors.get(2));
        return co;
    }
    private Tetrominoes(int[][] coords, Color co) {         
            
        this.coords = coords;
        color = co;
    }
}

