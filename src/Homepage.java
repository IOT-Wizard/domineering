import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage {
    private JFrame frame;
    private JComboBox<String> choiceComboBox;


    public Homepage() {
        frame = new JFrame("AI Home Page");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set layout manager
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the top right corner
        JPanel topPanel = new JPanel(new FlowLayout());


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
                frame.dispose(); // Close the current frame
                new BoardPage().setVisible(true); // Open the BoardPage
            }
        });
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your logic for starting a new game
                // For now, you can just display a message
                JOptionPane.showMessageDialog(frame, "New game started!");
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


        centerPanel.add(humanButton);
        centerPanel.add(vsLabel);
        centerPanel.add(choiceComboBox);
        centerPanel.add(newGameButton);


        // Add centerPanel to the main panel at the CENTER position
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Set frame properties
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Homepage();
            }
        });
    }
}
