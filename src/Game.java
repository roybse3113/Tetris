import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

@SuppressWarnings({ "serial", "unused" })
public class Game extends JFrame implements Runnable {
    
    private JLabel statusBar = new JLabel();
    static String username = "";
    private JTextField userInput = new JTextField("Username",20);
    private Map<String,Integer> players = new TreeMap<String,Integer>();
    File file = new File("files/Scores.txt");
    BufferedReader b;
    String line; 
    String output = "";
    private boolean running;
    
    public Map<String,Integer> getPlayers() {
        return players;
    }
    
    public void run() {
        
        running = true;
        
        statusBar = new JLabel("0");
        
        //Frame Label
        final JFrame frame = new JFrame("Tetris");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 3, screenSize.height / 9);
        
        //Status Panel
        JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusBar = new JLabel("0");
        statusPanel.add(statusBar);
        
        //Main Playing Area
        Board court = new Board(this);
        frame.add(court, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        
        //Reset Button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Board.isStarted) {
                    statusBar.setText("0");
                    court.reset();
                } else if (username.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a username",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    statusBar.setText("0");
                    court.reset();
                }
            }
        });
        
        //Start Button
        final JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e2) {
                if (Board.isStarted) {
                    court.setFocusable(true);
                } else if ((username.equals("") || username.contains(" "))) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid username",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                } else if (!Board.isStarted) {
                    statusBar.setText("0");
                    court.reset();
                }
                
                username = "";
                userInput.setText("Username");
            }
        });
        
        //Menu Button
        final JButton menu = new JButton("Menu");
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e3) {
                JOptionPane.showMessageDialog(null,
                        "1. You are playing Tetris, a classic! \n \n"
                        + "2. Your objective is to handle the shapes that fall \n \n"
                        + "and rotate them as needed in order to fit the shapes \n \n"
                        + "accordingly and fill an entire horizontal line. \n \n"
                        + "Do so before the shapes stack to the top!\n \n"
                        + "3. The right and left arrow keys are to horizontal movement. \n \n"
                        + "4. The up arrow is to turn the piece counter-clockwise. \n \n"
                        + "The down arrow is to move the piece down one row. \n \n"
                        + "5. More lines means more points. Good luck! \n \n"
                        + "6. Do not press the start button while playing. \n \n"
                        + "But, you may press restart once you have started.",
                        "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        //Enter Button
        final JButton enter = new JButton("Enter");
        BufferedWriter bw = null;
        FileWriter f = null;
        
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e4) {
    
                if (userInput.getText().length() == 0 || userInput.getText().contains(" ")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid username at least 1 character long"
                            + " and with no spaces",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    username = userInput.getText();
                    try {
                        PrintWriter writer = new PrintWriter(new File("files/Scores.txt"));
                        players.put(username, null);
                        StringBuilder s = new StringBuilder();
                        
                        s.append("Username: ");
                        s.append(username);
                        s.append("\n");
                        s.append("Score: ");
                        s.append(court.getLineCnt());
                        s.append("\n");
                        
                        writer.write(s.toString());

                        writer.close();
                        
                        
                    } catch (FileNotFoundException f) {
                        f.printStackTrace();
                    }  
                     /*
                     Use user name input to write into file
                     Also adds the user name into the Tree Map of players
                     The Tree Map named players stores the key user name
                     and a value of the score that will be used to sort 
                     high scores
                     */
                    
                    userInput.setText("");
                    JOptionPane.showMessageDialog(null,
                        "Success! Press start.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        //Add buttons to the panels
        controlPanel.add(reset);
        controlPanel.add(start);   
        controlPanel.add(userInput);
        controlPanel.add(enter); 
        statusPanel.add(menu);
        
        //Make visible on frame
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(500,900);
        
        
    }
    
    public JLabel getStatusBar() {
        return statusBar;
    }
    
    public static void main(String[] args) {
        Game tetris = new Game();
        tetris.setLocationRelativeTo(null);
        tetris.setVisible(true);
        SwingUtilities.invokeLater(new Game());
    }
}

 
