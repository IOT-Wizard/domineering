import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class Board extends JFrame {
    private JButton[][] buttons;
    private String currentPlayer;
    private int boardSize;

    public Board(String selectedSize , String player, boolean isAI) {
        boardSize = Integer.parseInt(selectedSize.substring(0, 1));
        buttons = new JButton[boardSize][boardSize];
        currentPlayer = "H" ; // 'H' for horizontal, 'V' for vertical
        initializeUI();
        //loadGameLevel(); // Load the game level at the start
        if (isAI) {
            performAIMove();
        }
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

            if (isValidMove( row, col)) {

                if (currentPlayer.equals("H")) {
                    buttons[row][col].setText("HH");
                    buttons[row + 1][col].setText("HH");
                } else {
                    buttons[row][col].setText("VV");
                    buttons[row][col + 1].setText("VV");
                }
                boolean win = wonPosition(currentPlayer);

                if (win) {
                    if (currentPlayer.equals("H"))  JOptionPane.showMessageDialog(getParent(), " player 1 win ");
                    if (currentPlayer.equals("Human"))  JOptionPane.showMessageDialog(getParent(), " player 2 win ");


                }

                // Switch players
                currentPlayer = (currentPlayer.equals("H")) ? "Human" : "H";
            }
        }
    }
    private void performAIMove() {
        // Create a Position object representing the current state of the board
        Position currentPosition = getCurrentBoardState();

        // Call the alpha-beta algorithm to get the best move
        Move bestMove = alphaBetaSearch(currentPosition, true);

        // Update the UI or perform any necessary actions based on the AI move
        updateBoardUI();

        // Switch players
        currentPlayer = "Human";
    }

    private Move alphaBetaSearch(Position currentPosition, boolean maximizingPlayer) {
        float alpha = Float.NEGATIVE_INFINITY;
        float beta = Float.POSITIVE_INFINITY;

        // Call the alpha-beta helper function
        Vector result = alphaBetaHelper(0, currentPosition, maximizingPlayer, alpha, beta);

        // Retrieve the best move from the result
        return (Move) result.elementAt(1);
    }

    private Vector alphaBetaHelper(int depth, Position p, boolean maximizingPlayer, float alpha, float beta) {
        if (reachedMaxDepth(p, depth) || wonPosition(p, true) || wonPosition(p, false)) {
            float value = positionEvaluation(p, maximizingPlayer);
            Vector v = new Vector();
            v.addElement(new Float(value));
            v.addElement(null);
            return v;
        }

        Vector best = new Vector();
        Position[] moves = possibleMoves(p, maximizingPlayer);

        for (int i = 0; i < moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !maximizingPlayer, -beta, -alpha);
            float value = -((Float) v2.elementAt(0)).floatValue();

            if (value >= beta) {
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement(); // skip previous value
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
                break; // Prune the branch
            }

            if (value > alpha) {
                alpha = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement(); // skip previous value
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
            }
        }

        Vector v3 = new Vector();
        v3.addElement(new Float((maximizingPlayer) ? alpha : beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        return v3;
    }

    private float positionEvaluation(Position p, boolean maximizingPlayer) {
        // Get the board state from the Position object
        String[][] boardState = ((ConcretePosition) p).getBoardState();

        // Example Evaluation Logic:
        // Calculate the score based on the number of dominos placed by each player.
        int player1Score = countDominos(boardState, "H");
        int player2Score = countDominos(boardState, "V");

        // Return the evaluation score (positive for maximizing player, negative for minimizing player)
        return maximizingPlayer ? player1Score - player2Score : player2Score - player1Score;
    }

    // Helper method to count the number of dominos for a specific player symbol
    private int countDominos(String[][] boardState, String playerSymbol) {
        int count = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[i].length; j++) {
                if (boardState[i][j].equals(playerSymbol)) {
                    count++;
                }
            }
        }
        return count;
    }


    private boolean reachedMaxDepth(Position p, int depth) {
       int MAX_DEPTH = 0;
        return depth >= MAX_DEPTH;
    }

    private boolean wonPosition(Position p, boolean maximizingPlayer) {
        // Get the board state from the Position object
        String[][] boardState = ((ConcretePosition) p).getBoardState();

        // Implement the winning condition based on the game rules
        // For example, check if there is a horizontal or vertical line of dominoes for the maximizingPlayer
        if (maximizingPlayer) {
            // Check for a horizontal line
            for (int i = 0; i < boardSize - 1; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (boardState[i][j].isEmpty() && boardState[i + 1][j].isEmpty()) {
                        return false;
                    }
                }
            }
        } else {
            // Check for a vertical line
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize - 1; j++) {
                    if (boardState[i][j].isEmpty() && boardState[i][j + 1].isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // If no winning condition is met, return false
        return true;
    }

    private Position[] possibleMoves(Position p, boolean maximizingPlayer) {
        // Get the board state from the Position object
        String[][] boardState = ((ConcretePosition) p).getBoardState();

        // Create an array to store the possible moves
        Position[] moves = new Position[2 * boardSize * boardSize]; // Maximum possible moves

        // Initialize the index for storing moves
        int moveIndex = 0;

        // Iterate through the board to find empty spots where a domino can be placed
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // Check for a horizontal move
                if (j < boardSize - 1 && boardState[i][j].isEmpty() && boardState[i][j + 1].isEmpty()) {
                    // Create a new position with the move applied
                    String[][] newBoardState = copyBoardState(boardState);
                    newBoardState[i][j] = "H";
                    newBoardState[i][j + 1] = "H";
                    moves[moveIndex++] = new ConcretePosition(newBoardState);
                }

                // Check for a vertical move
                if (i < boardSize - 1 && boardState[i][j].isEmpty() && boardState[i + 1][j].isEmpty()) {
                    // Create a new position with the move applied
                    String[][] newBoardState = copyBoardState(boardState);
                    newBoardState[i][j] = "V";
                    newBoardState[i + 1][j] = "V";
                    moves[moveIndex++] = new ConcretePosition(newBoardState);
                }
            }
        }

        // Trim the array to the actual number of moves
        return Arrays.copyOf(moves, moveIndex);
    }

    // Helper method to copy the board state
    private String[][] copyBoardState(String[][] boardState) {
        int size = boardState.length;
        String[][] copy = new String[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(boardState[i], 0, copy[i], 0, size);
        }
        return copy;
    }


    private Position getCurrentBoardState() {
        String[][] boardState = new String[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardState[i][j] = buttons[i][j].getText();
            }
        }

        return new ConcretePosition(boardState);
    }

    private void makeMove(Position p, boolean player, Move move) {
        // Ensure that the Position and Move are concrete instances
        if (!(p instanceof ConcretePosition) || !(move instanceof ConcreteMove)) {
            // Handle error or throw an exception
            return;
        }

        // Cast to concrete classes
        ConcretePosition concretePosition = (ConcretePosition) p;
        ConcreteMove concreteMove = (ConcreteMove) move;

        // Update the board state based on the move
        String[][] boardState = concretePosition.getBoardState();
        int row = concreteMove.getRow();
        int col = concreteMove.getCol();
        String playerSymbol = (player) ? "H" : "Human";

        // Check if the move is valid before updating the board state
        if (isValidMove(row, col)) {
            boardState[row][col] = playerSymbol;
        } else {
            // Handle invalid move (e.g., show a message or throw an exception)
            return;
        }

        // Update the UI or perform any necessary actions based on the move
        updateBoardUI();
    }

    private void updateBoardUI() {
        Position currentPosition = getCurrentBoardState();
        String[][] boardState = ((ConcretePosition) currentPosition).getBoardState();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText(boardState[i][j]);
            }
        }
    }


    private boolean isValidMove( int row, int col) {
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
        if(player == "Human"){
            for (int i = 0; i < boardSize-1; i++) { // taille = 5
                for (int j = 0; j < boardSize ; j++) {
                    if (i == boardSize-1) {//taille -1
                        continue;
                    } else if (buttons[i][j].getText().equals("") && buttons[i+1][j].getText().equals("")) {
                        return false;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < boardSize; i++) { // taille = 5
                for (int j = 0; j < boardSize-1 ; j++) {
                    if (buttons[i][j].getText().equals("") && buttons[i][j + 1].getText().equals("")) {
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
            for (int j = 0; j < boardSize - 1; j++) {
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
            // Retrieve the selected player from the homepage
            String selectedSize = HomePage.getSelectedSize();
            String selectedPlayer = HomePage.getSelectedPlayer();
            String selectedChoice = HomePage.getSelectedChoice();

            boolean isAI = selectedChoice.equals("AI") ||
                    selectedChoice.equals("AIlvl1") ||
                    selectedChoice.equals("AIlvl2");

            Board domineeringGame = new Board(selectedSize, selectedPlayer, isAI);
            domineeringGame.setLocationRelativeTo(null);
            domineeringGame.setVisible(true);
        });
    }
}