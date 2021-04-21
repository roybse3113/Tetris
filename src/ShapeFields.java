public interface ShapeFields {
    
    public Tetrominoes T = null;
    public int INDEX = 0;
    public int X = 0;
    public int Y = 0;   
    public Shape S = new Shape();
    public int[][] COORD = new int[4][2];
    public int[][] COORDCOPY = new int[4][2];
    
    public void setShape(Tetrominoes t);
    
    public int x(int index);
    
    public int y(int index);
    
    public void setX(int index, int x);
    
    public void setY(int index, int y);
    
    //Creates copy of setX for testing
    static void setXShape(int index, int x) {
        COORDCOPY[index][0] = x;
    }
    
    //Creates copy of setY for testing
    static void setYShape(int index, int y) {
        COORDCOPY[index][1] = y;
    }
    
    //Creates copy of x for testing
    static int xShape(int index) {
        return COORDCOPY[index][0];
    }
    
    //Creates copy of y for testing
    static int yShape(int index) {
        return COORDCOPY[index][1];
    }
    
    public void setCnt();
    
    public Tetrominoes getShape();
    
    public void setRandomShape();

    public Shape rotateLeft();
}
