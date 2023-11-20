import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BoardPage extends JFrame {
    private JButton[][] buttons;
    private char currentPlayer;

    public BoardPage() {
        buttons = new JButton[5][5];
        currentPlayer = 'H'; // 'H' for horizontal, 'V' for vertical

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Domineering Game");
        setSize(400, 500); // Increased height to accommodate buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the game grid
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(5, 5));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].addActionListener(new ButtonClickListener());
                gamePanel.add(buttons[i][j]);
            }
        }

        // Create a panel to hold the buttons (Hint)
        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Hint");
        hintButton.addActionListener(e -> showHint()); // Custom method for showing hints

        buttonPanel.add(hintButton);

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
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (buttons[i][j] == clickedButton) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            if (isValidMove(row, col)) {
                if (currentPlayer == 'H') {
                    buttons[row][col].setText("HH");
                    buttons[row + 1][col].setText("HH");
                } else {
                    buttons[row][col].setText("VV");
                    buttons[row][col + 1].setText("VV");
                }

                // Switch players
                currentPlayer = (currentPlayer == 'H') ? 'V' : 'H';
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        // Check if the move is within the bounds and the selected cells are empty
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            if (currentPlayer == 'H' &&
                    buttons[row][col].getText().equals("") &&
                    buttons[row + 1][col].getText().equals("")) {
                return true;
            } else return currentPlayer == 'V' &&
                    buttons[row][col].getText().equals("") &&
                    buttons[row][col + 1].getText().equals("");
        }
        return false;
    }

    private void showHint() {
        // Custom logic for showing a hint (you can implement your own hint mechanism)
        JOptionPane.showMessageDialog(this, "This is a hint!");
    }

    private void saveGameLevel() {
        // Custom logic for saving the game level (e.g., to a file or a database)
        JOptionPane.showMessageDialog(this, "Game level saved!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardPage domineeringGame = new BoardPage();
            domineeringGame.setLocationRelativeTo(null); // Center the window on the screen
            domineeringGame.setVisible(true);
        });
    }
}
