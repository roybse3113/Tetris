import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
    private Game tetris;
    
    @Test
    public void testResetWhileStarted() {    
        tetris = new Game();
        tetris.getStatusBar();
        Board board = new Board(tetris);
        
        tetris.run();
        
        board.start();
        assertTrue(Board.isStarted);
        
        board.reset();
        assertEquals((tetris.getStatusBar().getText()),"0"); 
        assertTrue(Board.isStarted);
        assertEquals(board.getLineCnt(),0);     
        Tetrominoes[] b = board.getBoard();
        
        for (int i = 0; i < board.area; i++) {
            assertEquals(b[i],Tetrominoes.NoShape);
        }
        
        // Upon starting after resetting, the board should still be clear
        board.start();
        
        for (int i = 0; i < board.area; i++) {
            assertEquals(b[i],Tetrominoes.NoShape);
        }
        board.reset();
    }
    
    @Test
    public void testFullUnfilledLine() {    
        tetris = new Game();
        Board board = new Board(tetris);
        
        tetris.run();        
        board.start();
        
        Tetrominoes[] pieces = new Tetrominoes[board.area];
                    
        
        // check to see if reset properly executed clearBoard() or cleared the board with NoShape
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = Tetrominoes.NoShape;
        }
        @SuppressWarnings("unused")
        Tetrominoes[] b = board.getBoard();
        b = pieces;    
        board.removeLines();
        assertEquals(board.getLineCnt(),0);
        board.reset();
    }
    
    @Test
    public void testShapeReturn() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        
        tetris.run();        
        board.start();
        
        
        piece.setShape(Tetrominoes.SquareShape);
        int[][] expected = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } };
        
        // Turning a Square does not change the coordinates
        
        piece.rotateLeft();
        
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(Shape.coordsCopy[i][j],expected[i][j]);
            }
        }
        board.reset();
    }
    
    @Test
    public void testPieceMovement() {  
        
        tetris = new Game();
        Board board = new Board(tetris);
        tetris.run();      
        board.start();
        Board.currentYCopy = 17;

        for (int i = 0; i < 17; i++) {
            board.oneLineDown();
            assertEquals(Board.currentYCopy,17 - i);
        }
        board.oneLineDown();
        // Once it reaches the bottom, it cannot move one line down
        // The Y resets for the next shape instead
        assertFalse(Board.currentYCopy == 0);
        
        board.reset();
      
    }
    
    @Test
    public void testFilledLine() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        
        tetris.run();        
        board.start();
        piece.setRandomShape();
        piece.setShape(piece.getShape());
        Tetrominoes[] b = board.getBoard();
        
        //Filled line on board will add 1 point
        for (int i = 0; i < board.bwidth; i++) {
            b[i] = piece.getShape();
        }
        board.removeLines();
        assertEquals(board.getLineCnt(),1);
        
        // Checks that line is removed by checking to see the line is empty
        for (int i = 0; i < board.bwidth; i++) {
            assertEquals(b[i],Tetrominoes.NoShape);
        }
        board.reset();
        
        // Checks that multiple lines are removed and added to count
        for (int i = 0; i < 3 * board.bwidth; i++) {
            b[i] = piece.getShape();
        }
        board.removeLines();
        assertEquals(board.getLineCnt(),6);
        board.reset();
    }
    
    @Test
    public void testPieceMoveAtEdge() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        piece.setShape(Tetrominoes.SShape);
        
        tetris.run();        
        board.start();
        
        assertTrue(board.canMove(piece, 0, board.bheight - 2));
        assertFalse(board.canMove(piece, -1, board.bheight - 2));
        assertFalse(board.canMove(piece, 1, board.bheight + 1));
        board.reset();
    }
    
    @Test
    public void testPieceMoveAtTop() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        
        //For squareShape - can turn when at 1 below height
        piece.setShape(Tetrominoes.SquareShape);
        
        tetris.run();        
        board.start();
        
        assertTrue(board.canMove(piece, 0, board.bheight - 1));
        
        //For SShape  - cannot turn when at 1 below height
        piece.setShape(Tetrominoes.LineShape);
        
        tetris.run();        
        board.start();
        
        assertFalse(board.canMove(piece, 0, board.bheight - 1));
  
    }
    
    @Test
    public void testLinePieceTurnAtEdge() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        piece.setShape(Tetrominoes.LineShape);
        
        tetris.run();        
        board.start(); 
        
        // Line shape cannot rotate when 1 block away from the left wall since it 
        // would not fit on board properly
        assertFalse(board.tryRotate(piece.rotateLeft(), 1, board.bheight - 2));
        
        // Line shape cannot rotate when 1 block away from the right wall since it would 
        // not fit on board properly
        assertFalse(board.tryRotate(piece.rotateLeft(), 
                board.bheight, board.bheight - 2));
        
        // Not possible for a shape to be above the height of the board
        assertFalse(board.tryRotate(piece.rotateLeft(), 3, board.bheight + 1));
        
        // The shape is within the bounds of the board and so is valid
        assertTrue(board.tryRotate(piece.rotateLeft(), 3, board.bheight - 2));
        board.reset();
    }
    
    @Test
    public void testSShapeValid() {    
        tetris = new Game();
        Board board = new Board(tetris);
        Shape piece = new Shape();
        piece.setShape(Tetrominoes.SShape);
        
        int[][] original = { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } };
        int[][] turn1 = { { -1, 0 }, { 0, 0 }, { 0, -1 }, { 1, -1 } };
        int[][] turn2 = { { 0, 1 }, { 0, 0 }, { -1, 0 }, { -1, -1 } };
        int[][] turn3 = { { 1, 0 }, { 0, 0 }, { 0, 1 }, { -1, 1 } };
        
        tetris.run();        
        board.start(); 
        
        //Ensures that the SShape coordinates are properly adjusted
        //with each turn
        
        piece.rotateLeft();
        
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertEquals(Shape.coordsCopy[i][j],turn1[i][j]);
            }
        }
        
        piece.rotateLeft();
        
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertEquals(Shape.coordsCopy[i][j],turn2[i][j]);
            }
        }
        
        piece.rotateLeft();
        
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertEquals(Shape.coordsCopy[i][j],turn3[i][j]);
            }
        }
        
        piece.rotateLeft();
        
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                assertEquals(Shape.coordsCopy[i][j],original[i][j]);
            }
        }

        board.reset();
    }
}

