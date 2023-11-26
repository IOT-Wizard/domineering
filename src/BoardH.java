import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
public class BoardH extends JFrame {
    private JButton[][] buttons;
    private String currentPlayer;
    private int boardSize;

    public BoardH(String selectedSize , String player) {
        boardSize = Integer.parseInt(selectedSize.substring(0, 1));
        buttons = new JButton[boardSize][boardSize];
        currentPlayer = "H" ; // 'H' for horizontal, 'V' for vertical
        initializeUI();
        //loadGameLevel(); // Load the game level at the start
    }


    private void initializeUI() {
        setTitle("Domineering Game");
        setSize(400, 500); // Increased height to accommodate buttons
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Create a panel to hold the game grid
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(5, 5));

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].addActionListener(new ButtonClickListener());
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
                saveGameLevel();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    private class ButtonClickListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            int row = -1, col = -1;

            // Find the clicked button's position in the grid
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (buttons[i][j] == clickedButton) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            if (isValidMove(row, col)) {
                boolean win = wonPosition(currentPlayer);

                if (win) {
                    String winnerMessage = (currentPlayer.equals("H")) ? "Human wins!" : "H wins!";
                    JOptionPane.showMessageDialog(getParent(), winnerMessage);
                }
                if (currentPlayer == "H") {
                    buttons[row][col].setText("HH");
                    buttons[row + 1][col].setText("HH");
                } else {
                    buttons[row][col].setText("VV");
                    buttons[row][col + 1].setText("VV");
                }

                // Switch players
                currentPlayer = (currentPlayer == "H") ? "Human" : "H";
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        // Check if the move is within the bounds and the selected cells are empty
        if (row >= 0 && row <= boardSize - 1 && col >= 0 && col <= boardSize - 1) {
            if (currentPlayer == "H" && buttons[row][col].getText().equals("") && buttons[row + 1][col].getText().equals("")) {
                return true;
            } else
                if (currentPlayer == "Human" && buttons[row][col].getText().equals("") && buttons[row][col + 1].getText().equals("")){
                return  true ;
            }


        }
        return false;
    }


    public boolean wonPosition(
            String player){
        if(player == "H"){
            for (int i = 0; i < 5; i++) { // taille = 5
                for (int j = 0; j < 5 ; j++) {
                    if (i == 4) {//taille -1
                        continue;
                    } else if (buttons[i][j].getText().equals("") && buttons[i+1][j].getText().equals("")) {
                        return false;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 5; i++) { // taille = 5
                for (int j = 0; j < 5 ; j++) {
                    if (j == 4) { // taille -1
                        continue;
                    } else if (buttons[i][j].getText().equals("") && buttons[i][j + 1].getText().equals("")) {
                        return false;
                    }
                }
            }

        }
        return  true ;
    }

    private void showHint() {
        // Find an empty spot where the player can place a domino

        for (int i = 0; i < boardSize-1; i++) {
            for (int j = 0; j < boardSize-1; j++) {
                if (buttons[i][j].getText().isEmpty() && buttons[i + 1][j].getText().isEmpty()) {
                    // Suggest a horizontal move
                    suggestHint(i, j, i + 1, j);
                    return;
                } else if (buttons[i][j].getText().isEmpty() && buttons[i][j + 1].getText().isEmpty()) {
                    // Suggest a vertical move
                    suggestHint(i, j, i, j + 1);
                    return;
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
            BoardH domineeringGame = new BoardH("5" , "Human");
            domineeringGame.setLocationRelativeTo(null);
            domineeringGame.setVisible(true);
        });
    }
}
