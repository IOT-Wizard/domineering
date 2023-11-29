import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class BoardIA extends JFrame {

    private DomineeringPosition currentPosition;
    private int boardSize;
    private boolean humanTurn;

    private JButton[][] buttons;
    private Domineering gameSearch;

    public BoardIA(String size) {
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
    }

    private void initializeGUI(int size) {
        setLayout(new GridLayout(size, size));

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(boardSize, boardSize));
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
                gamePanel.add(buttons[i][j]); // Add buttons to gamePanel instead of directly to the frame
            }
        }

        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> showHint());

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        buttonPanel.add(hintButton);
        buttonPanel.add(newGameButton);

        // Create a main panel to hold both the gamePanel and buttonPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Adjust the frame size based on its components
        setLocationRelativeTo(null);
        setVisible(true);
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
                if (humanTurn) {  // Check if it's the human player's turn
                    if (buttons[i][j].getBackground().equals(blankColor) && buttons[i + 1][j].getBackground().equals(blankColor)) {
                        // Suggest a horizontal move
                        suggestHint(i, j, i + 1, j);
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
        //  JOptionPane.showMessageDialog(this, "You can place a domino here!");
    }

    private void saveGameLevel() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_level.txt"))) {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    String cellState = buttons[i][j].getText().isEmpty() ? "E" : buttons[i][j].getText(); // "E" for empty
                    writer.write(cellState);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadGameLevel() {
        try (BufferedReader reader = new BufferedReader(new FileReader("game_level.txt"))) {
            for (int i = 0; i < boardSize; i++) {
                String line = reader.readLine();
                if (line != null) {
                    for (int j = 0; j < Math.min(line.length(), boardSize); j++) {
                        String cellState = String.valueOf(line.charAt(j));
                        buttons[i][j].setText(cellState);

                        // Enable or disable buttons based on cell state
                        if (cellState.equals("E")) {
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
        }
    }


    private void startNewGame() {
        int option = JOptionPane.showConfirmDialog(this,
                "Do you want to start a new game without saving the current one?",
                "Start New Game",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Check if there is a saved game and load it
            if (loadGameLevel()) {
                // If loading is successful, no need to clear the grid
                return;
            }

            // Clear the game grid if there is no saved game or loading failed
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    buttons[i][j].setText("");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardIA domineeringGame = new BoardIA("6");
            domineeringGame.setVisible(true);
        });
    }
}