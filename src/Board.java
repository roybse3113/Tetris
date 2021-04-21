import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
 
    final int bwidth = 10;
    final int bheight = 20;
    final int area = bwidth * bheight;
    private Timer timer;
    private boolean falling = false;
    static boolean isStarted = false;
    private boolean isPaused = false;
    private int lineCnt = 0;
    private int currentX = 0;
    static int currentXCopy = 0;
    private int currentY = 0;
    static int currentYCopy = 0;
    private JLabel statusBar;
    private Shape currentPiece = new Shape();
    private Tetrominoes[] board;
    private ArrayList<Integer> highScores = new ArrayList<Integer>();
    private Map<String,Integer> playerScores = new TreeMap<String,Integer>();
    boolean filled;
    int numFullLinesCopy;
    private Map<String,Integer> players;
    
    public Tetrominoes[] getBoard() {
        return board;
    }
    
    public int getLineCnt() {
        return lineCnt;
    }
 
    public boolean falling() {
        return falling;
    }
    
    public boolean started() {
        return isStarted;
    }
    
    public Board(Game tetris) {
        setFocusable(true);
    
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentPiece = new Shape();
    
        // Set up timer for falling pieces
        timer = new Timer(450, this); 
    
        // Prepare to update statusBar score 
        statusBar = tetris.getStatusBar();
        players = tetris.getPlayers();
    
        // Sets up the board as an array of Tetrominoes spanning the area of the board
        board = new Tetrominoes[area];
    
        clearBoard();
        addKeyListener(new MyTetrisAdapter());
    }
  
 //Methods to get the width and height of the shapes
    public int squareWidth() {
        return (int) getSize().getWidth() / bwidth;
    }
 
    public int squareHeight() {
        return (int) getSize().getHeight() / bheight;
    }
 
  
  //Checks the shape at a particular location on the board
    public Tetrominoes shapeOnBoard(int x, int y) {
    
        int pos = bwidth * y;
      
        return board[pos + x];
    }
 
  
  //Method to clear the board by filling it with empty shapes
    private void clearBoard() {
        for (int i = 0; i < area; i++) {
            board[i] = Tetrominoes.NoShape;
        }
    }
  
  
  //Implementation for start button functionality
    public void start() {
        if (isPaused) {
            return;
        }
   
        isStarted = true;
        falling = false;
        lineCnt = 0;
        clearBoard();
        nextPiece();
        timer.start();
    }
  
  //Implementation for reset button functionality
    public void reset() { 
        lineCnt = 0;
        start();
        requestFocusInWindow();
    }
 
  
  //Updating the dropping of each piece
    private void pieceDropped() {
      
        for (int i = 0; i < bwidth - 6; i++) {
            
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            int z = y * bwidth;
            
            board[x + z] = currentPiece.getShape();
        }
    
        removeLines();
   
        if (!falling) {
            nextPiece();
        }
    }
  
  //Prepares the next random piece to be dropped
    public void nextPiece() {
      
        int x = bwidth / 2;
        int y = bheight - 1;
        int yMin = currentPiece.minY();
      
        currentPiece.setRandomShape();
        currentX = 1 + x;
        currentXCopy = 1 + x;
        currentY = y + yMin;
        currentYCopy = y + yMin;
 
        if (!canMove(currentPiece, currentX, currentY - 1)) {
            currentPiece.setShape(Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            statusBar.setText("Game Over");
      
            highScores.clear();
      
      // Utilize Tree Map to access scores or points by user name entered
      
      // Begin to read and write user names and scores once the game is over
      
            for (Entry<String, Integer> entry : players.entrySet()) {
                String user = entry.getKey();
                players.replace(user, null, lineCnt);
            }
      
      // Order user names by value of score from highest to lowest

            for (Entry<String, Integer> entry : players.entrySet()) {
                String users = entry.getKey();
                int n = players.get(users);
                highScores.add(n);
            }
            Collections.sort(highScores,Collections.reverseOrder());

      // File IO handling for high scores and user names
      
      //String of players separated by whitespace
      
            String playerList = "";
         
            for (Entry<String, Integer> entry : players.entrySet()) {
                String user = entry.getKey();
                playerList += user + " ";
            }   
      
      
      // List of player names
      
            LinkedList<String> playerNames = new LinkedList<String>();
      
            for (String p : playerList.split(" ")) {
                if (!p.equals("")) {
                    playerNames.add(p);
                }
            }
      
      // Adding the player names to a new linked list that will be sorted by the scores
      
            LinkedList<String> keys2 = new LinkedList<String>();
            keys2.clear();
            keys2 = new LinkedList<String>();
      
            for (int i = 0; i < highScores.size(); i++) {        
                for (Entry<String,Integer> entry : players.entrySet()) {
                    for (Entry<String,Integer> entry2 : playerScores.entrySet()) {
                        if (entry2.getKey() == entry.getKey()) {
                            if (entry.getValue() < entry2.getValue()) {
                                players.replace(entry.getKey(), entry2.getValue());
                            }
                        }
                    }
                    
                    if (entry.getValue() == (highScores.get(i)) && 
                            !keys2.contains(entry.getKey())) {
                        keys2.add(entry.getKey());
                        playerScores.put(entry.getKey(), highScores.get(i));
                        break;             
                    }
                }
            }
    
      //Writing the names and scores of top 3 highest scores
            
            BufferedWriter bw = null;
            FileWriter f = null;
      
            try {
                f = new FileWriter("files/Scores.txt");
                bw = new BufferedWriter(f);
                for (String s : keys2) {
                    bw.write(s + ": " + players.get(s));
                    bw.write("\n");
                }
            } catch (IOException e) {
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (f != null) {
                        f.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
      
            File file = new File("files/Scores.txt");
            BufferedReader b;
            String line; 
            String line2; 
            String line3;
            String output = "";
      
            try {
                output = "";
                b = new BufferedReader(new FileReader(file));
                line = b.readLine();
                line2 = b.readLine();
                line3 = b.readLine();
   
                if (line != null && line2 != null && line3 != null) {
                    output += line + "\n" + line2 + "\n" + line3;
                    JOptionPane.showMessageDialog(null, output,
                            "High Scores", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough players have played."
                            + " There must be at least 3 players for high scores!",
                            "High Scores", JOptionPane.INFORMATION_MESSAGE);
                }
          
            } catch (FileNotFoundException f2) {
                f2.printStackTrace();
                throw new IllegalArgumentException();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }
 
    public void oneLineDown() {
        if (!canMove(currentPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (falling) {
            falling = false;
            nextPiece();
        } else {
            oneLineDown();
        }
    } 
 
    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
        int xBound = x + 1;
        int yBound = y + 1;
        int width = squareWidth() - 2;
        int height = squareHeight() - 2;
        Color color = shape.getColor();
    
        g.setColor(color);
        g.fillRect(xBound, yBound, width, height);

    }
 
  //Handling the drawing of each shape
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        int height = (int) getSize().getHeight();
        int heightBound = bheight - 1;
    
        int boardTop = height - squareHeight() * bheight;
 
        for (int i = 0; i < bheight; i++) {
            for (int j = 0; j < bwidth; ++j) {
                Tetrominoes shape = shapeOnBoard(j, heightBound - i);
 
                if (shape != Tetrominoes.NoShape) {
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }
 
        if (currentPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < bwidth - 6; ++i) {        
                int x = currentX + currentPiece.x(i);
                int y = currentY - currentPiece.y(i);
                int widthMult = x * squareWidth();
        
                drawSquare(g, widthMult, boardTop + squareHeight() * (heightBound - y), 
                        currentPiece.getShape());
            }
        }
    }
 
  //Checks to see is rotation is possible
    public boolean tryRotate(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < bwidth - 6; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
       
            if (newPiece.getShape() == Tetrominoes.LineShape) {
                if (x < 1 || x > bwidth - 1 || y > bheight - 1) {
                    return false;
                }
            } 
       
        }
      
        return true;
    }
 
   //Checks to see if the shape can move any more
    public boolean canMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < bwidth - 6; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
      
            if (x < 0 || x >= bwidth || y < 0 || y >= bheight) {
                return false;
            }
 
            if (shapeOnBoard(x, y) != Tetrominoes.NoShape) {
                return false;
            }
        }
 
        currentPiece = newPiece;
        currentX = newX;
        currentXCopy = newX;
        currentY = newY;
        currentYCopy = newY;
        repaint();
 
        return true;
    }    
 
  //Removes filled lines during the game and adds to the score
    public void removeLines() {
        int numFullLines = 0;
        numFullLinesCopy = 0;
 
        for (int i = bheight - 1; i >= 0; --i) {
        
            filled = true;
 
            for (int j = 0; j < bwidth; ++j) {
                if (shapeOnBoard(j, i) == Tetrominoes.NoShape) {
                    filled = false;
                    break;
                }
            }
 
            if (filled) {
                ++numFullLines;
                ++numFullLinesCopy;
 
                for (int k = i; k < bheight - 1; ++k) {
                    for (int j = 0; j < bwidth; ++j) {            
                        int width = bwidth * k;             
                        board[width + j] = shapeOnBoard(j, k + 1);
                    }
                }
            }
 
            if (numFullLines > 0) {
                lineCnt += numFullLines;
                statusBar.setText(String.valueOf(lineCnt));
                currentPiece.setShape(Tetrominoes.NoShape);
                falling = true;
                repaint();
            }
        }
    }
 
  //Moves the piece down to the board bottom
    public void down() {     
        int y = currentY;
        while (y > 0) {
            if (!canMove(currentPiece, currentX, y - 1)) {
                break;
            }
            --y;
            --currentYCopy;
        }
        pieceDropped();
    }
 
  //Handling for key functions and movement of shapes
    class MyTetrisAdapter extends KeyAdapter {
      
        @Override
        public void keyPressed(KeyEvent k) {
            if (!isStarted || currentPiece.getShape() == Tetrominoes.NoShape) {
                return;
            }
 
            int keys = k.getKeyCode(); 
            int xLeft = currentX - 1;
            int xRight = currentX + 1;
 
            switch (keys) {
                case KeyEvent.VK_LEFT:
                    canMove(currentPiece, xLeft, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    canMove(currentPiece, xRight, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    oneLineDown();
                    break;
                case KeyEvent.VK_UP:
                    //Checks specifically for line shape which cannot move when it is one 
                    // block away from the edges
                    if (currentPiece.getShape() == Tetrominoes.LineShape) {
                        if (currentX != 1 && currentX != 0 && currentX < bwidth - 1) {
                            canMove(currentPiece.rotateLeft(), currentX, currentY);
                            break;
                        } 
                    } else {
                        canMove(currentPiece.rotateLeft(), currentX, currentY);
                        break;
                    } 
                    break;
                case KeyEvent.VK_SPACE:
                    down();
                    break;
                default:
                    break;
            }
 
        }
    }

    public void actionPerformedSecond(ActionEvent e2) {
    }
 
}
