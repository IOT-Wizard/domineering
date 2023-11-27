import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage {
    private JFrame frame;
    private static String selectedSize;
    private static String player;
    private JComboBox<String> choiceComboBox;
    private JComboBox<String> sizeComboBox;
    private static String selectedChoice;

    public HomePage() {
        frame = new JFrame("AI Home Page");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set layout manager
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the top right corner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));



        // Add topPanel to the main panel at the PAGE_START position
        mainPanel.add(topPanel, BorderLayout.PAGE_START);

        // Create a panel for the center area
        JPanel centerPanel = new JPanel();

        // Add button for "Start a New Game"
        JButton newGameButton = new JButton("Start a New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to BoardPage when the button is clicked
                player = (String)choiceComboBox.getSelectedItem();
                selectedSize = (String) sizeComboBox.getSelectedItem();
                selectedChoice = (String) choiceComboBox.getSelectedItem(); // Store it in the static variable

                boolean isAI = selectedChoice.equals("AI") ||
                        selectedChoice.equals("AIlvl1") ||
                        selectedChoice.equals("AIlvl2");
                if (selectedSize != null) {
                    frame.dispose();
                    new Board(selectedSize, player, isAI).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a board size.");
                }
            }
        });

        JButton humanButton = new JButton("Human");
        humanButton.setPreferredSize(new Dimension(80, 30));


        // Add "VS" label
        JLabel vsLabel = new JLabel("VS");
        //JLabel titleLabel = new JLabel("Home Page ");

        // Add combo box for human or AI choice
        String[] choices = {"Human", "AI", "AIlvl1", "AIlvl2"};
        choiceComboBox = new JComboBox<>(choices);
        choiceComboBox.setPreferredSize(new Dimension(90, 30));



        // Create a panel for size board
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Size Board: ");
        String[] sizeOptions = {"2x2", "3x3","4x4","5x5"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);





        choiceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) sizeComboBox.getSelectedItem();
                if (selectedSize != null) {
                    // Transférer la taille sélectionnée à BoardPage
                    boolean isAI = selectedChoice.equals("AI") ||
                            selectedChoice.equals("AIlvl1") ||
                            selectedChoice.equals("AIlvl2");;
                    new Board(selectedSize , player , isAI);
                }

            }
        });





        // Add components to the center panel

        centerPanel.add(humanButton);

        centerPanel.add(vsLabel);

        centerPanel.add(choiceComboBox);

        centerPanel.add(sizePanel);
        centerPanel.add(newGameButton);

        // Add centerPanel to the main panel at the CENTER position
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Set frame properties
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static String getSelectedSize() {
        return selectedSize;
    }
    public static String getSelectedPlayer() {
        return player;
    }
    public static String getSelectedChoice(){
        return selectedChoice;
    }

    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage();
            }
        });
    }
}