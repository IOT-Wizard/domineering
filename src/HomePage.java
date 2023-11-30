import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HomePage {
    private static boolean load = false ;

    private JFrame frame;
    private static String selectedSize;
    private static String player;
    private JComboBox<String> choiceComboBox;
    private JComboBox<String> sizeComboBox;

    public HomePage() {
        frame = new JFrame("Home Page");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set layout manager
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        JPanel zeroRowPanel = new JPanel(new FlowLayout());
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        JPanel secondRowPanel = new JPanel(new FlowLayout());
        JPanel thirdRowPanel = new JPanel(new FlowLayout());









        // Add button for "Start a New Game"
        JButton newGameButton = new JButton("Start a New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to BoardPage when the button is clicked
                player = (String) choiceComboBox.getSelectedItem();
                selectedSize = (String) sizeComboBox.getSelectedItem();

                // Debug statements
                System.out.println("Selected Player: " + player);
                System.out.println("Selected Size: " + selectedSize);

                if (selectedSize != null) {
                    frame.dispose();
                    if ("Human".equals(player)) {  // Use equals() for string comparison
                        new BoardH(selectedSize, player, load).setVisible(true);
                    } else {
                        new BoardIA(selectedSize, player, load).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a board size.");
                }
            }
        });
        JLabel wLabel=new JLabel("Greetings, Game Explorer!");
        JButton humanButton = new JButton("Human");
        humanButton.setPreferredSize(new Dimension(80, 30));
        // Add "VS" label
        JLabel vsLabel = new JLabel("VS");


        // Add combo box for human or AI choice
        String[] choices = {"Human", "AI", "AIlvl1", "AIlvl2"};
        choiceComboBox = new JComboBox<>(choices);
        choiceComboBox.setPreferredSize(new Dimension(90, 30));

        // Create a panel for size board
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Size Board: ");
        String[] sizeOptions = {"3x3","4x4","5x5","8x8"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);


        sizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) sizeComboBox.getSelectedItem();
                if (selectedSize != null) {
                    // Transférer la taille sélectionnée à BoardPage
                    System.out.println(selectedSize);

                }

            }
        });




        choiceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String player = (String) choiceComboBox.getSelectedItem();
                if (player != null) {
                    // Transférer la taille sélectionnée à BoardPage
                    System.out.println(player);

                }

            }
        });


        newGameButton.setBackground(new Color(0xDDE0EE));
        humanButton.setBackground(new Color(0xA3AEDA));
        choiceComboBox.setBackground(new Color(0xA3AEDA));
        // Add components to the MAIN panel
        zeroRowPanel.add(wLabel);
        firstRowPanel.add(humanButton);
        firstRowPanel.add(vsLabel);
        firstRowPanel.add(choiceComboBox);
        thirdRowPanel.add(sizePanel);
        mainPanel.add(zeroRowPanel);
        mainPanel.add(firstRowPanel);
        mainPanel.add(thirdRowPanel);

        // Add newGameButton to the second row of mainPanel
        secondRowPanel.add(newGameButton);
        // Add secondRowPanel to the mainPanel
        mainPanel.add(secondRowPanel);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Set frame properties
        frame.setVisible(true);

    }
    public static String getSelectedSize() {
        return selectedSize;
    }
    public static String getSelectedPlayer() {
        return player;
    }

    public static boolean getload(){
        return load;
    }
    private boolean loadGame(String fileName , int boardSize , String p) {
        BoardH board = new BoardH(String.valueOf(boardSize), p , false);
        return board.loadGameLevel(fileName);
    }

    private void startSavedGame() {
        int option = JOptionPane.showConfirmDialog(
                frame,
                "Do you want to continue the saved game?",
                "Continue Saved Game",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            load = true;

            try (BufferedReader reader = new BufferedReader(new FileReader("game_level.txt"))) {
                // Read board size from the first line
                String sizeLine = reader.readLine();
                if (sizeLine != null) {
                    int boardSize = Integer.parseInt(sizeLine.trim());

                    // Read player information from the second line
                    String playerLine = reader.readLine();
                    if (playerLine != null) {
                        String player = playerLine.trim();

                        // Print for debugging
                        System.out.println("Player: " + player + ", Board Size: " + boardSize);
                        frame.dispose();
                        // Create the BoardH instance
                        BoardH board =new BoardH(String.valueOf(boardSize), player, load);
                        board.setVisible(true);

                        // Uncomment the following block if the 'loadGame' method is defined
                    /*
                    if (loadGame("game_level.txt", boardSize, player)) {
                        JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
                    }
                    */
                    } else {
                        // Player information not found
                        JOptionPane.showMessageDialog(frame, "Player information not found. Starting a new game.");
                    }
                } else {
                    // Board size not found
                    JOptionPane.showMessageDialog(frame, "Board size not found. Starting a new game.");
                }
            } catch (IOException | NumberFormatException e) {
                // Handle specific exceptions appropriately
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage homepage = new HomePage();
            homepage.startSavedGame(); // Call the method to check for saved games at the beginning
        });
    }
}