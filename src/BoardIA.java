import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class BoardIA extends JFrame {

    private DomineeringPosition currentPosition;
    private int boardSize;
    private boolean humanTurn;

    private JButton[][] buttons;
    private Domineering gameSearch;

    int HintH=3;
    int HintV=3;


    public BoardIA(String size, Boolean load) {
        super("Domineering Game");

        if (size != null && !size.isEmpty()) {
            boardSize = Integer.parseInt(size.substring(0, Math.min(size.length(), 1)));
        } else {
            System.out.println("Invalid size value. Using default size.");
            boardSize = 5;
        }

        currentPosition = new DomineeringPosition(boardSize);
        humanTurn = true;
        gameSearch = new Domineering();

        buttons = new JButton[boardSize][boardSize];

        initializeGUI(boardSize);

        if(load)  loadGameLevel("game_level.txt"); // Load the game level at the start
    }

    private void initializeGUI(int size) {
        //setLayout(new GridLayout(size, size));
        setSize(500, 600);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                startNewGame();
            }
        });

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(size, size));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                final int row = i, col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(row, col);
                    }
                });
                buttons[i][j].setBackground(Color.white);
                gamePanel.add(buttons[i][j]);
            }
        }

        // Create a panel to hold the buttons (Hint)
        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> showHint()); // Custom method for showing hints

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        buttonPanel.add(hintButton);
        buttonPanel.add(newGameButton);

        // Create a main panel to hold the game panel and button panel vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel);

        // Add the main panel to the center of the frame
        add(mainPanel, BorderLayout.CENTER);

        // Add a window listener to save the game level when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(BoardIA.this,
                        "Voulez-vous sauvegarder la partie en cours avant de quitter?",
                        "Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    saveGameLevel();
                    System.exit(0);
                }
            }
        });
    }

    private void handleButtonClick(int row, int col) {
        if (humanTurn && isValidMove(row, col)) {
            buttons[row][col].setBackground(new Color(0x8D0808));
            buttons[row][col + 1].setBackground(new Color(0x8D0808));
            currentPosition.board[row * currentPosition.size + col] = DomineeringPosition.HUMAN;
            currentPosition.board[row * currentPosition.size + col + 1] = DomineeringPosition.HUMAN;

            if (checkWin(DomineeringPosition.HUMAN)) {
                JOptionPane.showMessageDialog(this, "Human won!");
                resetGame();
                return;
            }

            humanTurn = false;
            makeProgramMove();
            humanTurn = true;

            if (checkWin(DomineeringPosition.PROGRAM)) {
                JOptionPane.showMessageDialog(this, "Program won!");
                resetGame();
            }
        }
    }

    private boolean checkWin(int player) {
        return currentPosition.wonPosition(player);
    }

    private void makeProgramMove() {
        currentPosition.makeProgramMove();
        updateGUI();
        if (checkWin(DomineeringPosition.PROGRAM)) {
            JOptionPane.showMessageDialog(this, "Program won!");
            resetGame();
        }
    }

    private void updateGUI() {
        for (int i = 0; i < currentPosition.size; i++) {
            for (int j = 0; j < currentPosition.size; j++) {
                int index = i * currentPosition.size + j;
                if (currentPosition.board[index] == DomineeringPosition.PROGRAM) {
                    buttons[i][j].setBackground(new Color(0x070707));
                }
            }
        }

        if (checkWin(DomineeringPosition.PROGRAM)) {
            JOptionPane.showMessageDialog(this, "Program won!");
            resetGame();
        }
    }

    private void resetGame() {
        currentPosition = new DomineeringPosition(currentPosition.size);
        humanTurn = true;

        for (int i = 0; i < currentPosition.size; i++) {
            for (int j = 0; j < currentPosition.size; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        // Check if the move is within the bounds
        if (row >= 0 && row <= boardSize - 1 && col >= 0 && col <= boardSize - 1) {

            Color backgroundColor = buttons[row][col].getBackground();
            Color blankColor = Color.WHITE;
            if ((( backgroundColor.equals(blankColor) || backgroundColor.equals(Color.YELLOW)) &&
                    ( buttons[row][col + 1].getBackground().equals(blankColor)|| buttons[row][col + 1].getBackground().equals(Color.yellow)) )) {
                return true;
            }}
        return false;
    }

    private void showHint() {
        Color blankColor = Color.WHITE; // Adjust this to the actual background color of blank buttons

        for (int i = 0; i < boardSize - 1; i++) {
            for (int j = 0; j < boardSize - 1; j++) {
                if (humanTurn && HintH > 0) {
                    if (buttons[i][j].getBackground().equals(blankColor) && buttons[i + 1][j].getBackground().equals(blankColor)) {
                        // Suggest a horizontal move
                        suggestHint(i, j, i + 1, j);
                        HintH--;
                        return;
                    }

                }
            }
        }

        // If no valid move is found, provide a generic hint
        JOptionPane.showMessageDialog(this, "No valid moves available.");
    }



    private void suggestHint(int row1, int col1, int row2, int col2) {
        buttons[row1][col1].setBackground(Color.YELLOW);
        buttons[row2][col2].setBackground(Color.YELLOW);

        // Display a hint message
        JOptionPane.showMessageDialog(this, "You can place a domino here!");
    }

    private void saveGameLevel() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_level.txt"))) {
            writer.write(String.valueOf(boardSize));
            writer.newLine();

            // Save the player on the second line
            writer.write(String.valueOf(humanTurn));
            writer.newLine();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    String cellState;
                    Color backgroundColor = buttons[i][j].getBackground();
                    if (backgroundColor.equals(new Color(0x8D0808)) || backgroundColor.equals(new Color(0x070707))) {
                        // Save the colors for non-empty cells (black or red)
                        cellState = backgroundColor.equals(new Color(0x8D0808)) ? "H" : "P";
                    } else {
                        // Save "0" for empty cells
                        cellState = "0";
                    }

                    writer.write(cellState);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadGameLevel(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line ;

            int currentLineNumber = 0;
            while ((line = reader.readLine()) != null && currentLineNumber < 1) {
                currentLineNumber++;

                // Si la ligne actuelle est la ligne cible, imprimez-la
                if (currentLineNumber == 2) {
                    System.out.println("Ligne " + 2 + ": " + line);
                }
            }

            for (int i = 0; i < boardSize; i++) {
                line = reader.readLine();
                System.out.println("Ligne " + 2 + ": " + line);

                if (line != null) {
                    for (int j = 0; j < Math.min(line.length(), boardSize ); j++) {
                        String cellState = String.valueOf(line.charAt(j));

                        // Update the button based on the saved cellState
                        if (cellState.equals("H")) {
                            buttons[i][j].setBackground(new Color(0x8D0808));
                            // buttons[i + 1][j].setBackground(new Color(0x8D0808));
                        } else if (cellState.equals("V")) {
                            buttons[i][j].setBackground(new Color(0x070707));
                            //buttons[i][j + 1].setBackground(new Color(0x070707));
                        } else {
                            // "0" represents empty cells
                            buttons[i][j].setBackground(Color.WHITE);
                            // buttons[i][j + 1].setBackground(Color.WHITE);
                            // buttons[i + 1][j].setBackground(Color.WHITE);
                            //buttons[i + 1][j + 1].setBackground(Color.WHITE);
                        }

                        // Enable or disable buttons based on cell state
                        if (cellState.equals("0")) {
                            buttons[i][j].setEnabled(true); // Enable empty cells
                        } else {
                            buttons[i][j].setEnabled(false); // Disable non-empty cells
                        }
                    }
                }
            }
            return true; // Loading successful
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Loading failed
            //rds
        }
    }



    private void startNewGame() {
        int option = JOptionPane.showConfirmDialog(this,
                "start a new   game ?",
                "Start New Game",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new HomePage() ;
        }


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardIA domineeringGame = new BoardIA(HomePage.getSelectedSize(),  HomePage.getload());
            domineeringGame.setVisible(true);
        });
    }
}